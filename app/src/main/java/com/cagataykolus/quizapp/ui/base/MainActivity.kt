package com.cagataykolus.quizapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cagataykolus.quizapp.databinding.ActivityMainBinding
import com.cagataykolus.quizapp.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Çağatay Kölüş on 19.09.2021.
 * cagataykolus@gmail.com
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}