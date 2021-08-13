package com.rus_artur4ik.databasecoursework

data class TargetItem(
    val id: Int,
    val title: String,
    val subtargets: List<Checkbox>
) {

    data class Checkbox(
        val id: Int,
        val target_id: Int,
        val title: String,
        val isChecked: Boolean
    )
}