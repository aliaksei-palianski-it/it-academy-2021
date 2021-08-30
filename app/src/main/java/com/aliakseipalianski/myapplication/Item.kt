package com.aliakseipalianski.myapplication

data class Item(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val type: String = "none",
) {
    companion object {
        fun generate(): List<Item> {
            val itemList = ArrayList<Item>()

            repeat(50) {
                itemList.add(Item(it, "first name $it", "last name $it"))
            }

            return itemList
        }
    }
}