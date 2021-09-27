package com.rus_artur4ik.databasecoursework

data class TargetItem(
    val id: Int,
    val title: String,
    val subtargets: List<Subtarget>
) {
    interface Subtarget {
        val id: Int
        val target_id: Int
    }

    data class Checkbox(
        override val id: Int,
        override val target_id: Int,
        val title: String,
        val isChecked: Boolean
    ): Subtarget

    data class ProgressBar(
        override val id: Int,
        override val target_id: Int,
        val title: String,
        val maxProgress: Int,
        val progress: Int
    ): Subtarget
}