package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var priorityList: List<PriorityModel> = mutableListOf()
    private var taskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener {
            hadleSave()
        }
        binding.buttonDate.setOnClickListener {
            handleDate()
        }

        viewModel.loadPriorities()

        loadDataFromActivity()

        //Observer
        observe()

        // Layout
        setContentView(binding.root)
    }

    private fun loadDataFromActivity(){
        val bundle = intent.extras
        if(bundle != null){
            taskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.loadTask(taskId)
        }
    }

    private fun getIndex(priorityId: Int): Int{
        var index = 0
        for(i in priorityList){
            if(i.id == priorityId){
                break
            }
            index++
        }
        return index
    }

    private fun observe(){
        viewModel.priorityList.observe(this){
            priorityList = it
            val list = mutableListOf<String>()
            for(i in it){
                list.add(i.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }

        viewModel.taskSave.observe(this){
            if(it.status()){
                if (taskId == 0){
                    showToast("Tarefa cadastrada com sucesso")
                }else{
                    showToast("Tarefa atualizada com sucesso")
                }

                finish()
            }else{
                showToast(it.message())
            }
        }

        viewModel.task.observe(this){
            binding.editDescription.setText(it.description)
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))
            binding.checkComplete.isChecked = it.complete

            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        }

        viewModel.taskLoad.observe(this){
            if(!it.status()){
                showToast(it.message())
            }
        }
    }

    private fun hadleSave(){
        val task = TaskModel().apply {
            this.id = taskId
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()

            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = priorityList[index].id
        }

        if(taskId == 0) {
            viewModel.create(task)
        }else{
            viewModel.update(task)
        }
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this,
            { view, year, month, dayOfMonth ->
                //função onDateSet
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val dueDate = dateFormat.format(calendar.time)
                binding.buttonDate.text = dueDate
            }, year, month, day
        ).show()
    }

    private fun showToast(msg: String){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}