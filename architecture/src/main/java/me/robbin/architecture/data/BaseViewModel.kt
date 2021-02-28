package me.robbin.architecture.data

import androidx.lifecycle.AndroidViewModel
import me.robbin.architecture.utils.Utils


/**
 * @PackageName: me.robbin.architecture.data
 * @Name:        BaseViewModel
 * @Description: Base ViewModel
 * @UpdateDate:  2021/2/28 20:20
 * @UpdateUser:  Robbin Ma
 * Created by Robbin Ma in 2021/2/28 20:20
 */

abstract class BaseViewModel(): AndroidViewModel(Utils.getApp()) {
}