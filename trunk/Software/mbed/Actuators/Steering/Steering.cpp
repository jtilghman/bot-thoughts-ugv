#include "Steering.h"
#include "math.h"

/** create a new steering calculator for a particular vehicle
 *
 */
Steering::Steering(float wheelbase, float track)
    : _wheelbase(wheelbase)
    , _track(track)
    , _intercept(2.0)
{
}

void Steering::setIntercept(float intercept)
{
    _intercept = intercept;
}

/** Calculate a steering angle based on relative bearing
 *
 */
float Steering::calcSA(float theta)
{
    return calcSA(theta, -1.0); // call with no limit
}

/** calcSA
 * minRadius -- radius limit (minRadius < 0 disables limiting)
 */
float Steering::calcSA(float theta, float minRadius)
{
    float radius;
    float SA;
    bool neg = (theta < 0);

    // I haven't had time to work out why the equation is slightly offset such
    // that negative angle produces slightly less steering angle
    //
    if (neg) theta = -theta;

    // The equation peaks out at 90* so clamp theta artifically to 90, so that
    // if theta is actually > 90, we select max steering
    if (theta > 90.0) theta = 90.0;

    // Compute |radius| based on intercept distance and specified angle with extra gain to
    // overcome steering slop, misalignment, sidehills, etc.
    radius = _intercept / ( 2 * sin(angle_radians(theta)) );

    if (minRadius > 0) {
        if (radius < minRadius) radius = minRadius;
    }

    // Now calculate steering angle based on wheelbase and track width
    SA = angle_degrees(asin(_wheelbase / (radius - _track/2)));
    // The above ignores the effect of speed on required steering angle.
    // Even when under the limits of traction, understeer means more angle
    // is required to achieve a turn at higher speeds than lower speeds.
    // To consider this, we'd need to measure the understeer gradient of
    // the vehicle (thanks to Project240 for this insight) and include
    // that in the calculation.

    if (neg) SA = -SA;

    return SA;
}

/**
 * Bxy - robot coordinates
 * Axy - previous waypoint coords
 * Cxy - next waypoint coords
 */
float Steering::crossTrack(float Bx, float By, float Ax, float Ay, float Cx, float Cy)
{
    // Compute rise for prev wpt to bot; or compute vector offset by A(x,y)
    float Rx = (Bx - Ax);
    // compute run for prev wpt to bot; or compute vector offset by A(x,y)
    float Ry = (By - Ay);
    // dx is the run for the path
    float dx = Cx - Ax;
    // dy is the rise for the path
    float dy = Cy - Ay;
    // this is hypoteneuse length squared
    float ACd2 = dx*dx+dy*dy;
    // length of hyptoenuse
    // float ACd = sqrtf( ACd2 );

    float Rd = Rx*dx + Ry*dy;
    float t = Rd / ACd2;
    // nearest point on current segment
    float Nx = Ax + dx*t;
    float Ny = Ay + dy*t;
    // Cross track error
    float NBx = Nx-Bx;
    float NBy = Ny-By;
    float cte = sqrtf(NBx*NBx + NBy*NBy);

    return cte;
}


float Steering::pathPursuitSA(float hdg, float Bx, float By, float Ax, float Ay, float Cx, float Cy)
{
    float SA;
    // Leg vector
    float Lx = Cx - Ax;
    float Ly = Cy - Ay;
    // Robot vector
    float Rx = Bx - Ax;
    float Ry = Cy - Ay;

    // Find the goal point, a projection of the bot vector onto the current leg, moved ahead
    // along the path by the lookahead distance
    float legLength = sqrtf(Lx*Lx + Ly*Ly); // ||L||
    float proj = (Lx*Rx + Ly*Ry)/legLength; // R dot L/||L||, projection magnitude, bot vector onto leg vector
    float LAx = (proj+_intercept)*Lx/legLength + Ax; // find projection point + lookahead, along leg
    float LAy = (proj+_intercept)*Ly/legLength + Ay;
    // Compute a circle that is tangential to bot heading and intercepts bot
    // and goal point (LAx,LAy), the intercept circle. Then compute the steering
    // angle to trace that circle.
    float brg = 180*atan2(LAx-Rx,LAy-Ry)/PI;
    // would be nice to add in some noise to heading info
    float relBrg = brg-hdg;
    if (relBrg < -180.0)
        relBrg += 360.0;
    if (relBrg >= 180.0)
        relBrg -= 360.0;
    // I haven't had time to work out why the equation is asymmetrical, that is, 
    // negative angle produces slightly less steering angle. So instead, just use
    // absolute value and restore sign later.
    float sign = (relBrg < 0) ? -1 : 1;
    // The equation peaks out at 90* so clamp theta artifically to 90, so that
    // if theta is actually > 90, we select max steering
    if (relBrg > 90.0) relBrg = 90.0;
    // Compute radius based on intercept distance and specified angle
    float radius = _intercept/(2*sin(fabs(relBrg)*PI/180));
    // optionally, limit radius min/max
    // Now compute the steering angle to achieve the circle of 
    // Steering angle is based on wheelbase and track width
    SA = sign * angle_degrees(asin(_wheelbase / (radius - _track/2)));

    return SA;
}


float Steering::purePursuitSA(float hdg, float Bx, float By, float Ax, float Ay, float Cx, float Cy)
{
    float SA;

    // Compute rise for prev wpt to bot; or compute vector offset by A(x,y)
    float Rx = (Bx - Ax);
    // compute run for prev wpt to bot; or compute vector offset by A(x,y)
    float Ry = (By - Ay);
    // dx is the run for the path
    float dx = Cx - Ax;
    // dy is the rise for the path
    float dy = Cy - Ay;
    // this is hypoteneuse length squared
    float ACd2 = dx*dx+dy*dy;
    // length of hyptoenuse
    float ACd = sqrtf( ACd2 );

    float Rd = Rx*dx + Ry*dy;
    float t = Rd / ACd2;
    // nearest point on current segment
    float Nx = Ax + dx*t;
    float Ny = Ay + dy*t;
    // Cross track error
    float NBx = Nx-Bx;
    float NBy = Ny-By;
    float cte = sqrtf(NBx*NBx + NBy*NBy);
    float NGd;

    float myLookAhead;

    if (cte <= _intercept) {
        myLookAhead = _intercept;
    } else {
        myLookAhead = _intercept + cte;
    }

    NGd = sqrt( myLookAhead*myLookAhead - cte*cte );
    float Gx = NGd * dx/ACd + Nx;
    float Gy = NGd * dy/ACd + Ny;

    float hdgr = hdg*PI/180;

    float BGx = (Gx-Bx)*cos(hdgr) - (Gy-By)*sin(hdgr);
    float c = (2 * BGx) / (myLookAhead*myLookAhead);

    float radius;

    if (c != 0) {
        radius = 1/c;
    } else {
        radius = 999999.0;
    }

    // Now calculate steering angle based on wheelbase and track width
    SA = angle_degrees(asin(_wheelbase / (radius - _track/2)));

    return SA;
}
