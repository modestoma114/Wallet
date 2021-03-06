package me.robbin.utils


/**
 * @PackageName: me.robbin.utils
 * @Name:        StringUtils
 * @Description: String Tools
 * @UpdateDate:  2021/3/5 10:18
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/5 10:18
 */

object StringUtils {

    /**
     * @Description: Determine if the string is white space
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:23
     */
    fun isSpace(s: String): Boolean {
        s.forEach {
            if (!Character.isWhitespace(it)) {
                return false
            }
        }
        return true
    }

}