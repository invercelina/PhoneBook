package com.example.phonebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.phonebook.ui.theme.PhoneBookTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhoneBookTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        val db = remember {
            AppDatabase.getDatabase(context)
        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var name by remember { mutableStateOf("") }
            var phoneNumber by remember { mutableStateOf("") }
            var emailAddress by remember { mutableStateOf("") }
            val myUserList = remember { mutableStateListOf<MyUser>() }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "이름") },
                placeholder = { Text(text = "이름") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(text = "전화번호") },
                placeholder = { Text(text = "전화번호") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = emailAddress,
                onValueChange = { emailAddress = it },
                label = { Text(text = "이메일") },
                placeholder = { Text(text = "이메일") },
                modifier = Modifier.fillMaxWidth()

            )
            val coroutineScope = rememberCoroutineScope()

            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    db.userDao().insertAll(
                        User(
                            userName = name,
                            phoneNumber = phoneNumber,
                            emailAddress = emailAddress
                        )
                    )
                }
            }) {
                Text(text = "등록")
            }
            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    coroutineScope.launch(Dispatchers.IO){
                        db.userDao().deleteAll()
                    }
                }
            }) {
                Text(text = "삭제")
            }
            Column {
                val userList by db.userDao().getAll().collectAsState(initial = emptyList())
                userList.forEach {user ->
                    Text(text = "이름 : ${user.userName}")
                    Text(text = "전화번호 : ${user.phoneNumber}")
                    Text(text = "이메일 : ${user.emailAddress}")
                }
            }

//            myUserList.forEach { user ->
//                Text(text = "이름 : ${user.name}", modifier = Modifier.fillMaxWidth())
//                Text(text = "전화번호 : ${user.phoneNumber}", modifier = Modifier.fillMaxWidth())
//                Text(text = "이메일 : ${user.emailAddress}", modifier = Modifier.fillMaxWidth())

        }
    }


    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        PhoneBookTheme {
            MainScreen()
        }
    }

}

data class MyUser(val name: String, val phoneNumber: String, val emailAddress: String)