package com.example.rerng_app_report_project.Data_rerngApp

class APIState<T>(

    val state: State,
    val movies: T? = null,
    val errorMessage: String? = null
)

enum class State {
    ERROR, SUCCESS
}