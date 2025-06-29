package com.furniture.app.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.furniture.app.data.IkeaAPI
import com.furniture.app.data.LlmActivity
import com.furniture.app.data.SupabaseActivity
import io.github.jan.supabase.SupabaseClient

class DesignVM(supabase: SupabaseClient, context: Context) : ViewModel() {
    var llmActivity= LlmActivity()
    var ikeaAPI= IkeaAPI()
    var supabaseActivity= SupabaseActivity(supabase, context)

    var imgUrl = { mutableStateOf("") }

    


}