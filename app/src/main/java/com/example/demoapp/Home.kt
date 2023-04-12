package com.example.demoapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

@Suppress("DEPRECATION")
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var deviceListAdapter: ArrayAdapter<String>
    private val devices = mutableListOf<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        deviceListAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, devices)
        view.findViewById<ListView>(R.id.deviceList).adapter = deviceListAdapter
        view.findViewById<Button>(R.id.scanbtn).setOnClickListener {
            listPairedDevices()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            listPairedDevices()
        }
    }

    @SuppressLint("MissingPermission")
    private fun listPairedDevices() {
        devices.clear()
        deviceListAdapter.notifyDataSetChanged()
        val pairedDevices = bluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                devices.add("${device.name} (${device.address})")
            }
            deviceListAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(requireContext(), "No paired devices found", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}