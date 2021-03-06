package me.robbin.utils

import android.net.Uri
import android.os.Build
import java.io.*


/**
 * @PackageName: me.robbin.utils
 * @Name:        FileUtils
 * @Description: File Tools
 * @UpdateDate:  2021/3/5 10:13
 * @UpdateUser:  Robbin Ma
 * Create by Robbin Ma in 2021/3/5 10:13
 */

object FileUtils {

    private val LIN_SEP = System.getProperty("line.separator")

    /**
     * @Description: Create files from path
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 上午10:25
     */
    fun getFileByPath(filePath: String?): File? {
        if (filePath == null) return null
        return if (StringUtils.isSpace(filePath)) null else File(filePath)
    }

    /**
     * @Description: Determine if a file exists
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 上午10:26
     */
    fun isFileExists(file: File): Boolean {
        if (file.exists()) return true
        return isFileExists(file.absolutePath)
    }

    /**
     * @Description: Determine if a file exists based on its path
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 上午10:26
     */
    fun isFileExists(filePath: String): Boolean {
        val file = getFileByPath(filePath) ?: return false
        if (file.exists()) return true
        return isFileExistsAboveQ(filePath)
    }

    /**
     * @Description: Determine if a file exists in API Version Q or higher
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 上午10:26
     */
    private fun isFileExistsAboveQ(filePath: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val uri = Uri.parse(filePath)
                val contentResolver = Utils.getApp().contentResolver
                val assetDescriptor = contentResolver.openAssetFileDescriptor(uri, "r") ?: return false
                try {
                    assetDescriptor.close()
                } catch (e: IOException) {
                }
            } catch (e: IOException) {
                return false
            }
            return true
        }
        return false
    }

    /**
     * @Description: Rename files according to path
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:37
     */
    fun rename(filePath: String, newName: String): Boolean {
        return rename(getFileByPath(filePath), newName)
    }

    /**
     * @Description: Rename files
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:37
     */
    fun rename(file: File?, newName: String): Boolean {
        if (file == null) return false
        if (!file.exists()) return false
        if (StringUtils.isSpace(newName)) return false
        if (newName == file.name) return true
        val newFile = File("${file.parent}/$newName")
        return !newFile.exists() && file.renameTo(newFile)
    }

    /**
     * @Description: Determine if it is a directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:46
     */
    fun isDir(dirPath: String?): Boolean {
        return isDir(getFileByPath(dirPath))
    }

    /**
     * @Description: Determine if it is a directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:46
     */
    fun isDir(file: File?): Boolean {
        return file != null && file.exists() && file.isDirectory
    }

    /**
     * @Description: Determine if it is a file
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:49
     */
    fun isFile(filePath: String?): Boolean {
        return isFile(getFileByPath(filePath))
    }

    /**
     * @Description: Determine if it is a file
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:49
     */
    fun isFile(file: File?): Boolean {
        return file != null && file.exists() && file.isFile
    }

    /**
     * @Description: Determine if the directory exists, and if not, determine if it was created successfully
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:52
     */
    fun createOrExistsDir(dirPath: String?): Boolean {
        return createOrExistsDir(getFileByPath(dirPath))
    }

    /**
     * @Description: Determine if the directory exists, and if not, determine if it was created successfully
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 10:52
     */
    fun createOrExistsDir(file: File?): Boolean {
        return file != null && (if (file.exists()) file.isDirectory else file.mkdirs())
    }

    /**
     * @Description: Determine if the file exists, and if not,m determine if it was created successfully
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:03
     */
    fun createOrExistsFile(filePath: String?): Boolean {
        return createOrExistsFile(getFileByPath(filePath))
    }

    /**
     * @Description: Determine if the file exists, and if not,m determine if it was created successfully
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:03
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        if (!createOrExistsDir(file.parentFile)) return false
        return try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * @Description: Determine if the file exists, and delete it before it is created
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:26
     */
    fun createFileOrDeleteOldFile(filePath: String?): Boolean {
        return createFileOrDeleteOldFile(getFileByPath(filePath))
    }

    /**
     * @Description: Determine if the file exists, and delete it before it is created
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:26
     */
    fun createFileOrDeleteOldFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists() && !file.delete()) return false
        if (!createOrExistsDir(file.parentFile)) return false
        return try {
            return file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * @Description: Copy a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:33
     */
    fun copy(srcPath: String, destPath: String): Boolean {
        return copy(getFileByPath(srcPath), getFileByPath(destPath), null)
    }

    /**
     * @Description: Copy a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:33
     */
    fun copy(
        srcPath: String,
        destPath: String,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        return copy(getFileByPath(srcPath), getFileByPath(destPath), replaceCallback)
    }

    /**
     * @Description: Copy a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:33
     */
    fun copy(src: File?, dest: File?): Boolean {
        return copy(src, dest, null)
    }

    /**
     * @Description: Copy a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:33
     */
    fun copy(
        src: File?,
        dest: File?,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        if (src == null) return false
        if (src.isDirectory) return copyDir(src, dest, replaceCallback)
        return copyFile(src, dest, replaceCallback)
    }

    /**
     * @Description: Copy directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:33
     */
    fun copyDir(
        srcDir: File?,
        destDir: File?,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        return copyOrMoveDir(srcDir, destDir, false, replaceCallback)
    }

    /**
     * @Description: Copy file
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 11:33
     */
    fun copyFile(
        srcFile: File?,
        destFile: File?,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        return copyOrMoveFile(srcFile, destFile, false, replaceCallback)
    }

    /**
     * @Description: Move a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:44
     */
    fun move(srcPath: String, destPath: String): Boolean {
        return move(getFileByPath(srcPath), getFileByPath(destPath), null)
    }

    /**
     * @Description: Move a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:44
     */
    fun move(
        srcPath: String,
        destPath: String,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        return move(getFileByPath(srcPath), getFileByPath(destPath), replaceCallback)
    }

    /**
     * @Description: Move a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:44
     */
    fun move(src: File?, dest: File?): Boolean {
        return move(src, dest, null)
    }

    /**
     * @Description: Move a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:44
     */
    fun move(
        src: File?,
        dest: File?,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        if (src == null) return false
        if (src.isDirectory) return moveDir(src, dest, replaceCallback)
        return moveFile(src, dest, replaceCallback)
    }

    /**
     * @Description: Move directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:44
     */
    fun moveDir(
        srcDir: File?,
        destDir: File?,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        return copyOrMoveDir(srcDir, destDir, true, replaceCallback)
    }

    /**
     * @Description: Move file
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:44
     */
    fun moveFile(
        srcFile: File?,
        destFile: File?,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        return copyOrMoveFile(srcFile, destFile, true, replaceCallback)
    }

    fun copyOrMoveDir(
        srcDir: File?,
        destDir: File?,
        isMove: Boolean,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        if (srcDir == null || destDir == null) return false
        val srcPath: String = srcDir.path + File.separator
        val destPath: String = destDir.path + File.separator
        if (destPath.contains(srcPath)) return false
        if (!srcDir.exists() || !srcDir.isDirectory) return false
        if (!createOrExistsDir(destDir)) return false
        val files = srcDir.listFiles()
        if (files != null && files.isNotEmpty()) {
            files.forEach {
                val oneDestFile: File = File(destPath + it.name)
                if (it.isFile)
                    if (!copyOrMoveFile(it, oneDestFile, isMove, replaceCallback)) return false
                    else if (it.isDirectory)
                        if (!copyOrMoveDir(it, oneDestFile, isMove, replaceCallback)) return false
            }
        }
        return isMove || deleteDir(srcDir)
    }

    fun copyOrMoveFile(
        srcFile: File?,
        destFile: File?,
        isMove: Boolean,
        replaceCallback: ((srcFile: File?, destFile: File?) -> Boolean)?
    ): Boolean {
        if (srcFile == null || destFile == null) return false
        if (srcFile == destFile) return false
        if (!srcFile.exists() || !srcFile.isFile) return false
        if (destFile.exists()) {
            if (replaceCallback == null || replaceCallback.invoke(srcFile, destFile)) {
                if (!destFile.delete()) return false
            } else {
                return true
            }
        }
        if (!createOrExistsDir(destFile.parentFile)) return false
        return try {
            FileIOUtils.writeFileFromIS(destFile.absolutePath, FileInputStream(srcFile))
                    && !(isMove && !deleteFile(srcFile))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * @Description: Delete a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:59
     */
    fun delete(filePath: String): Boolean {
        return delete(getFileByPath(filePath))
    }

    /**
     * @Description: Delete a file or directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 13:59
     */
    fun delete(file: File?): Boolean {
        if (file == null) return false
        if (file.isDirectory) return deleteDir(file)
        return deleteFile(file)
    }

    /**
     * @Description: Delete file
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 下午2:01
     */
    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * @Description: Delete directory
     * @Params:
     * @Return:
     * Created by Robbin Ma in 2021/3/5 下午2:34
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        if (!dir.exists()) return true
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            files.forEach {
                if (!it.delete()) return false
                else if (it.isDirectory) if (!deleteDir(dir)) return false
            }
        }
        return dir.delete()
    }

    fun deleteAllInDir(dirPath: String): Boolean {
        return deleteAllInDir(getFileByPath(dirPath))
    }

    fun deleteAllInDir(dir: File?): Boolean {
        return deleteFilesInDirWithFilter(dir, object : FileFilter {
            override fun accept(pathname: File?): Boolean = true
        })
    }

    fun deleteFilesInDir(dirPath: String): Boolean {
        return deleteFilesInDir(getFileByPath(dirPath))
    }

    fun deleteFilesInDir(dir: File): Boolean {
        return deleteFilesInDirWithFilter(dir, object : FileFilter {
            override fun accept(pathname: File?): Boolean = pathname?.isFile ?: false
        })
    }

    fun deleteFilesInDirWithFilter(dirPath: String, filter: FileFilter): Boolean {
        return deleteFilesInDirWithFilter(getFileByPath(dirPath), filter)
    }

    fun deleteFilesInDirWithFilter(dir: File?, filter: FileFilter): Boolean {
        if (dir == null) return false
        if (!dir.exists()) return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            files.forEach {
                if (filter.accept(it)) {
                    if (it.isFile) {
                        if (!it.delete()) return false
                    } else if (it.isDirectory) {
                        if (!deleteDir(it)) return false
                    }
                }
            }
        }
        return true
    }

    fun listFilterInDir(dirPath: String): List<File> {
        return listFilterInDir(dirPath, null)
    }

    fun listFilterInDir(dir: File): List<File> {
        return listFilterInDir(dir, null)
    }

    fun listFilterInDir(dirPath: String, comparator: Comparator<File>) {
        return listFilterInDir()
    }

}