package views

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.messagecounter.SmsData
import com.example.messagecounter.databinding.FragmentMessageCounterBinding
import kotlin.collections.ArrayList
import android.database.Cursor
import android.provider.Telephony
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.messagecounter.Conversation
import com.example.messagecounter.Message
import java.lang.NumberFormatException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class FragmentMessageCounter : Fragment() {

    lateinit var binding: FragmentMessageCounterBinding
    private val RequestReadSms: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessageCounterBinding.inflate(inflater,container, false)
        checkPermissionAndReadContacts()
        binding.submitButton.setOnClickListener {
            checkPermissionAndReadContacts()
        }
        return binding.root
    }

    private fun checkPermissionAndReadContacts(){
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_SMS), RequestReadSms)
        }else{
            readMessages(requireContext())
            }
        }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun readMessages(context: Context) {
        try {
            val cursor = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            val numbers = ArrayList<String>()
            val messages = ArrayList<Message>()
//            val results = ArrayList<Conversation>()
            val numInput = binding.etNumber.text.toString()
            val daysInput = binding.etDays.text.toString().toInt()
            val dateList = ArrayList<Date>()

            while (cursor != null && cursor.moveToNext()) {
                val smsDate = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                val number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))

                if (number != null && number == numInput){
                    numbers.add(number)
                    messages.add(Message(number, body, Date(smsDate.toLong())))
                }else{
                    binding.result.text = "0 Message Found"
                }
            }

            cursor?.close()
            for(i in 0 until daysInput){
                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -i)
                val decDate = cal.time
                dateList.add(decDate)
            }

            var count = 0
            for (i in 0 until messages.size){
                for (j in 0 until dateList.size){
                    if (messages[i].date < dateList[j]){
                        count+=1
                    }
                }
            }
            binding.result.text = "$count  Message Found"

        }catch (e:NumberFormatException){
            e.printStackTrace()
        }
    }
}



