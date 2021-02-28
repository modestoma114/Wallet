package me.robbin.architecture.func

import android.util.SparseArray
import me.robbin.architecture.data.BaseViewModel


/**
 * @PackageName: me.robbin.architecture.func
 * @Name:        DataBindingConfig
 * @Description: Configuration of DataBinding
 * @UpdateDate:  2021/2/28 23:24
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 23:24
 */

class DataBindingConfig(
    private val layout: Int,
    private val vmVariableId: Int,
    private val stateViewModel: BaseViewModel
) {

    private var bindingParams: SparseArray<Any> = SparseArray()

    fun getLayout(): Int = layout

    fun getVmVariableId(): Int = vmVariableId

    fun getStateViewModel(): BaseViewModel = stateViewModel

    fun getDataBindingParams(): SparseArray<Any> = bindingParams

    fun addBindingParam(variableId: Int, objezt: Any): DataBindingConfig {
        if (bindingParams.get(variableId) == null) {
            bindingParams.put(variableId, objezt)
        }
        return this
    }

}