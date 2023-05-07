package com.example.demoapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class Home : Fragment() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var deviceListAdapter: ArrayAdapter<String>
    private val devices = mutableListOf<String>()
    private lateinit var socket: BluetoothSocket
    private lateinit var device: BluetoothDevice

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        deviceListAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, devices)
        view.findViewById<ListView>(R.id.deviceList).adapter = deviceListAdapter
        view.findViewById<Button>(R.id.scanbtn).setOnClickListener {
            scanForDevices()
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

        view.findViewById<ListView>(R.id.deviceList).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                connectToDevice(position)
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

    @SuppressLint("MissingPermission")
    private fun scanForDevices() {
        devices.clear()
        deviceListAdapter.notifyDataSetChanged()

        if (!bluetoothAdapter.startDiscovery()) {
            Toast.makeText(requireContext(), "Discovery failed to start.", Toast.LENGTH_SHORT).show()
            return
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireActivity().registerReceiver(receiver, filter)
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null && !devices.contains(
                        "${device.name} (${device.address})"
                )) {
                    devices.add("${device.name} (${device.address})")
                    deviceListAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun connectToDevice(position: Int) {
        val deviceString = deviceListAdapter.getItem(position)
        val deviceAddress = deviceString?.substring(deviceString.indexOf("(") + 1, deviceString.length - 1)
        deviceAddress?.let {
            device = bluetoothAdapter.getRemoteDevice(it)
            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID)
                socket.connect()
                Toast.makeText(requireContext(), "Device connected", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Log.e(TAG, "Error connecting to device: ${e.message}")
                Toast.makeText(requireContext(), "Error connecting to device", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Invalid device address", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_DISCOVERABLE_BT = 2
        private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private const val TAG = "HomeFragment"
    }



}