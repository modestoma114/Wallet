package me.robbin.architecture.ext

import java.lang.reflect.ParameterizedType


/**
 * @PackageName: me.robbin.architecture.ext
 * @Name:        ViewModelExt
 * @Description: ViewModel Extensions
 * @UpdateDate:  2021/2/28 23:44
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 23:44
 */

@Suppress("UNCHECKED_CAST")
fun <VM> getVmCls(cls: Any): VM {
    return (cls.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}