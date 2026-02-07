package com.example.eventapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

data class Event(
    val id: Int,
    val title: String,
    val location: String,
    val date: String
)

class DashboardActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var title: EditText
    lateinit var location: EditText
    lateinit var date: EditText

    lateinit var createBtn: Button
    lateinit var updateBtn: Button
    lateinit var deleteBtn: Button
    lateinit var joinBtn: Button
    lateinit var participantsBtn: Button
    lateinit var logoutBtn: Button

    val events = ArrayList<Event>()
    val selectedIds = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        listView = findViewById(R.id.eventList)
        title = findViewById(R.id.title)
        location = findViewById(R.id.location)
        date = findViewById(R.id.date)

        createBtn = findViewById(R.id.createBtn)
        updateBtn = findViewById(R.id.updateBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        joinBtn = findViewById(R.id.joinBtn)
        participantsBtn = findViewById(R.id.participantsBtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        loadEvents()

        listView.setOnItemClickListener { _, _, position, _ ->
            val eventId = events[position].id
            if (selectedIds.contains(eventId)) selectedIds.remove(eventId)
            else selectedIds.add(eventId)
        }

        createBtn.setOnClickListener { createEvent() }
        updateBtn.setOnClickListener { updateEvent() }
        deleteBtn.setOnClickListener { deleteEvents() }
        joinBtn.setOnClickListener { joinEvent() }
        participantsBtn.setOnClickListener { viewParticipants() }
        logoutBtn.setOnClickListener { logout() }
    }

    // ================= LOAD EVENTS =================
    private fun loadEvents() {
        thread {
            val response = URL(
                "http://10.0.2.2/Kazi/library_management_api/index.php?module=event&action=list"
            ).readText()

            events.clear()
            val arr = JSONArray(response)

            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                events.add(
                    Event(
                        o.getInt("id"),
                        o.getString("title"),
                        o.getString("location"),
                        o.getString("event_date")
                    )
                )
            }

            runOnUiThread {
                listView.adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_multiple_choice,
                    events.map {
                        "ID: ${it.id}\n${it.title}\n${it.location} | ${it.date}"
                    }
                )
                selectedIds.clear()
            }
        }
    }

    // ================= CREATE =================
    private fun createEvent() {
        if (title.text.isEmpty() || location.text.isEmpty() || date.text.isEmpty()) {
            Toast.makeText(this, "Jaza taarifa zote", Toast.LENGTH_LONG).show()
            return
        }

        thread {
            val url =
                "http://10.0.2.2/Kazi/library_management_api/index.php?module=event&action=create" +
                        "&title=" + URLEncoder.encode(title.text.toString(), "UTF-8") +
                        "&location=" + URLEncoder.encode(location.text.toString(), "UTF-8") +
                        "&date=" + URLEncoder.encode(date.text.toString(), "UTF-8")

            val response = URL(url).readText()

            runOnUiThread {
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                title.text.clear()
                location.text.clear()
                date.text.clear()
                loadEvents()
            }
        }
    }

    // ================= UPDATE =================
    private fun updateEvent() {
        if (selectedIds.size != 1) {
            Toast.makeText(this, "Chagua event MOJA tu", Toast.LENGTH_LONG).show()
            return
        }

        val id = selectedIds.first()

        thread {
            val url =
                "http://10.0.2.2/Kazi/library_management_api/index.php?module=event&action=update" +
                        "&event_id=$id" +
                        "&title=" + URLEncoder.encode(title.text.toString(), "UTF-8") +
                        "&location=" + URLEncoder.encode(location.text.toString(), "UTF-8") +
                        "&date=" + URLEncoder.encode(date.text.toString(), "UTF-8")

            val response = URL(url).readText()

            runOnUiThread {
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                loadEvents()
            }
        }
    }

    // ================= DELETE (MULTI) =================
    private fun deleteEvents() {
        if (selectedIds.isEmpty()) {
            Toast.makeText(this, "Chagua event", Toast.LENGTH_LONG).show()
            return
        }

        thread {
            selectedIds.forEach {
                URL(
                    "http://10.0.2.2/Kazi/library_management_api/index.php?module=event&action=delete&event_id=$it"
                ).readText()
            }

            runOnUiThread {
                Toast.makeText(this, "Events deleted", Toast.LENGTH_LONG).show()
                loadEvents()
            }
        }
    }

    // ================= JOIN =================
    private fun joinEvent() {
        if (selectedIds.size != 1) {
            Toast.makeText(this, "Chagua event MOJA tu", Toast.LENGTH_LONG).show()
            return
        }

        val id = selectedIds.first()

        thread {
            val response = URL(
                "http://10.0.2.2/Kazi/library_management_api/index.php?module=event&action=join&event_id=$id"
            ).readText()

            runOnUiThread {
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
            }
        }
    }

    // ================= VIEW PARTICIPANTS (DIALOG) =================
    private fun viewParticipants() {
        thread {
            val response = URL(
                "http://10.0.2.2/Kazi/library_management_api/index.php?module=event&action=participants"
            ).readText()

            val list = ArrayList<String>()
            val arr = JSONArray(response)

            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                list.add(
                    "Member: ${o.getString("member")}\n" +
                            "Event: ${o.getString("event")}\n" +
                            "Joined: ${o.getString("joined_at")}"
                )
            }

            runOnUiThread {
                showParticipantsDialog(list)
            }
        }
    }

    private fun showParticipantsDialog(data: List<String>) {
        val lv = ListView(this)
        lv.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            data
        )

        AlertDialog.Builder(this)
            .setTitle("BOOKS BORROWED")
            .setView(lv)
            .setPositiveButton("CLOSE") { d, _ -> d.dismiss() }
            .show()
    }

    // ================= LOGOUT =================
    private fun logout() {
        thread {
            URL(
                "http://10.0.2.2/Kazi/library_management_api/index.php?module=account&action=logout"
            ).readText()

            runOnUiThread {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
