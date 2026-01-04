package com.example.rightperson.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rightperson.roomDB.Dao.NegativeDao
import com.example.rightperson.roomDB.Dao.PersonDao
import com.example.rightperson.roomDB.Dao.PositiveDao
import com.example.rightperson.roomDB.Dao.ResumeNegativeDao
import com.example.rightperson.roomDB.Dao.ResumePositiveDao
import com.example.rightperson.roomDB.Tables.Negative
import com.example.rightperson.roomDB.Tables.Person
import com.example.rightperson.roomDB.Tables.Positive
import com.example.rightperson.roomDB.Tables.ResumeNegative
import com.example.rightperson.roomDB.Tables.ResumePositive

@Database(
    // классы, которые тут написаны, автоматически с котлина переведутся на sql
    entities = [Positive::class, Negative::class, Person::class, ResumePositive::class, ResumeNegative::class],
    version = 1,
)
abstract class DB : RoomDatabase() {

    abstract fun getPositiveDao(): PositiveDao
    abstract fun getNegativeDao(): NegativeDao
    abstract fun getPersonDao(): PersonDao
    abstract fun getResumePositiveDao(): ResumePositiveDao
    abstract fun getResumeNegativeDao(): ResumeNegativeDao

    companion object {
        fun getDatabase(context: Context): DB =
            Room
                .databaseBuilder(
                    // получает все ресурсы (разрешения на работу с сетью, с диском и тд), доступные приложению
                    context = context.applicationContext,
                    // класс, который описывает саму базу данных (::class.java - позволяет получить класс джава - для просмотра его структуры). чтобы он сам смог написать методы
                    klass = DB::class.java,
                    // название файла базы данных
                    name = "RightPersonDB.db",
                ).addCallback(onDatabaseCreate) // добавить коллбэк (отложенный вызов функции) - создать отложенный вызов функции после создания базы (когда база есть, но она пустая)
                .build() // собрать базу данных

        // переменная как экземпляр класса (тип перeменной) для реализации абстрактных функций
        private var onDatabaseCreate: Callback =
            object : Callback() { // создаём сам объект, наследующий класс коллбэк
                override fun onCreate(db: SupportSQLiteDatabase) { // реализуем функции
                    db.execSQL(
                        "INSERT INTO Positive(title) VALUES " +
                                "('Optimistic')," +
                                "('Thoughtful');"

                    ) // вручную добавляем записи в таблицу Positive
                    db.execSQL(
                        "INSERT INTO Negative(title) VALUES " +
                                "('Arrogant')," +
                                "('Conceited');"
                    )
                }
            }
    }
}