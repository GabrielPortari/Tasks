package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun create(task: TaskModel, listener: APIListener<Boolean>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.create(task.priorityId,
            task.description,
            task.dueDate,
            task.complete)

        executeCall(call, listener)
    }

    fun update(task: TaskModel, listener: APIListener<Boolean>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remote.update(task.id,
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete)

        executeCall(call, listener)
    }

    fun list(listener: APIListener<List<TaskModel>>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.list()
        list(call, listener)
    }

    fun listNext7Days(listener: APIListener<List<TaskModel>>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.listNext7Days()
        list(call, listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.listOverdue()
        list(call, listener)
    }

    private fun list(call: Call<List<TaskModel>>, listener: APIListener<List<TaskModel>>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(call, listener)
    }

    fun delete(id: Int, listener: APIListener<Boolean>) {
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.delete(id)
        executeCall(call, listener)
    }

    fun load(id: Int, listener: APIListener<TaskModel>) {
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.load(id)
        executeCall(call, listener)
    }

    fun complete(id: Int, listener: APIListener<Boolean>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.complete(id)
        executeCall(call, listener)
    }

    fun undo(id: Int, listener: APIListener<Boolean>){
        if(!isConnectinAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.undo(id)
        executeCall(call, listener)
    }
}