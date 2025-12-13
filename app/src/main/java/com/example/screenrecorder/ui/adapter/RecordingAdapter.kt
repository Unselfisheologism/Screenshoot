package com.example.screenrecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.screenrecorder.databinding.ItemRecordingBinding
import com.example.screenrecorder.model.RecordingFile
import com.example.screenrecorder.util.FileUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordingAdapter(
    private val recordings: MutableList<RecordingFile>,
    private val onAction: (RecordingFile, String) -> Unit
) : RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingViewHolder {
        val binding = ItemRecordingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordingViewHolder(binding, onAction)
    }
    
    override fun onBindViewHolder(holder: RecordingViewHolder, position: Int) {
        holder.bind(recordings[position])
    }
    
    override fun getItemCount() = recordings.size
    
    fun updateList(newRecordings: List<RecordingFile>) {
        recordings.clear()
        recordings.addAll(newRecordings)
        notifyDataSetChanged()
    }
    
    class RecordingViewHolder(
        private val binding: ItemRecordingBinding,
        private val onAction: (RecordingFile, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(recording: RecordingFile) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val dateString = dateFormat.format(Date(recording.timestamp))
            
            binding.tvFileName.text = recording.fileName
            binding.tvFileInfo.text = "${FileUtils.getFormattedFileSize(recording.fileSize)} • $dateString"
            
            binding.btnPlay.setOnClickListener {
                onAction(recording, "play")
            }
            
            binding.btnEdit.setOnClickListener {
                onAction(recording, "edit")
            }
            
            binding.btnShare.setOnClickListener {
                onAction(recording, "share")
            }
            
            binding.btnDelete.setOnClickListener {
                onAction(recording, "delete")
            }
        }
    }
}
