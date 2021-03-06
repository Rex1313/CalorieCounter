package com.example.caloriecounter.utils.export

import com.example.caloriecounter.CsvConvertible
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry
import java.io.File

class CsvConverter {

    companion object {
        lateinit var dataDirectory: String
        fun saveToCsv(list: List<CsvConvertible>, fileName: String) {
            val fileDirectories = File(dataDirectory)
            fileDirectories.mkdirs()
            val file = File(fileDirectories, fileName)
            file.createNewFile()
                file.printWriter().use { writer ->
                list.forEach {
                    writer.println(it.toCsv())
                }
            }
        }

        inline fun <reified T : CsvConvertible> readFromCsv(fileName: String): List<T> {
            val file = File(dataDirectory, fileName).also {
                it.parentFile.mkdirs()
            }
            val list = mutableListOf<T>()
            file.useLines {
                println(it)
                it.forEach {
                    val values = it.split("|")
                    when (T::class) {
                        Entry::class -> {
                            list.add(
                                Entry(null,
                                    values[1],
                                    values[2].toFloat(),
                                    values[3],
                                    values[4]
                                ) as T
                            )
                        }
                        DailySetting::class -> {
                            list.add(
                                DailySetting(
                                    values[0],
                                    values[1].toInt()
                                ) as T
                            )
                        }
                    }
                }
            }
            return list
        }
    }
}