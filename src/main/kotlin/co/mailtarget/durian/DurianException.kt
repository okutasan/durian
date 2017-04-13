package co.mailtarget.durian

/**
 *
 * @author masasdani
 * @since 4/7/17
 */
class DurianException(override val message: String) : Exception(message){
    constructor(): this("Extract Exception")
}