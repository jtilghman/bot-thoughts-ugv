
#define PI 3.141592653589

/** A class for managing steering angle calculations based on current and desired heading
 *  and specified intercept distance along the new path.  
 *
 *  See Notebook entry: http://mbed.org/users/shimniok/notebook/smooth-steering-for-rc-car/
 */
class Steering
{
  public:
    /** create a new steering calculator
     *
     * @param wheelbase vehicle wheelbase
     * @param track vehicle track width
     * @param intercept new course intercept distance
     */
    Steering(float wheelbase, float track);

    /** set intercept distance
     * @param intercept distance along new course at which turn arc will intercept
     */
    void setIntercept(float intercept);

    /** convert course change to average steering angle
     * assumes Ackerman steering, with track and wheelbase
     * and course intercept distance specified.
     *
     * See notebook: http://mbed.org/users/shimniok/notebook/smooth-steering-for-rc-car/
     *
     * @param theta relative bearing of the new course
     * @returns steering angle in degrees
     */ 
    float calcSA(float theta);

    /** convert course change to average steering angle
     * assumes Ackerman steering, with track and wheelbase
     * and course intercept distance specified. Also, |radius| of turn is limited to limit
     *
     * See notebook: http://mbed.org/users/shimniok/notebook/smooth-steering-for-rc-car/
     *
     * @param theta relative bearing of the new course
     * @param limit is the limit of the turn circle radius (absolute value)
     * @returns steering angle in degrees
     */ 
    float calcSA(float theta, float limit);

    /** compute steering angle based on pure pursuit algorithm
     */
    float purePursuitSA(float hdg, float Bx, float By, float Ax, float Ay, float Cx, float Cy);
    
    /** compute steering angle based on a simpler path pursuit variant of pure pursuit
     */
    float pathPursuitSA(float hdg, float Bx, float By, float Ax, float Ay, float Cx, float Cy);

    /** Compute cross track error given last waypoint, next waypoint, and robot coordinates
     * @returns cross track error
     */
    float crossTrack(float Bx, float By, float Ax, float Ay, float Cx, float Cy);
    
  private:

    inline static float angle_radians(float deg) {return (PI/180.0)*deg;}

    inline static float angle_degrees(float rad) {return (180/PI)*rad;}

    float _wheelbase;
    float _track;
    float _intercept;
};
