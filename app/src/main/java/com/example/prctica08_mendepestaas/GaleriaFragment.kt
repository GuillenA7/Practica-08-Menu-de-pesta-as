package com.example.prctica08_mendepestaas

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView


class GaleriaFragment : Fragment() {
    // componentes
    private lateinit var view: View
    private lateinit var spnCanciones: Spinner
    private var cancionSelected: String = "Undertale OST - Undertale"

    private lateinit var volumen : SeekBar
    private lateinit var reproduccion : SeekBar

    private lateinit var tiempo: TextView

    // musica
    private var Home: MediaPlayer? = null
    private var repHome = false

    private var Memory: MediaPlayer? = null
    private var repMemory = false

    private var Undertale: MediaPlayer? = null
    private var repUndertale = false

    // handler
    private val handler = android.os.Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_galeria, container, false)

        spnCanciones = view.findViewById(R.id.spnCanciones)
        volumen = view.findViewById(R.id.sbVolumen)
        reproduccion = view.findViewById(R.id.sbReproduccion)
        tiempo = view.findViewById(R.id.txtReproduccion)

        // inicializar musica
        Home = MediaPlayer.create(context, R.raw.home)
        Home?.isLooping = true

        Memory = MediaPlayer.create(context, R.raw.memory)
        Memory?.isLooping = true

        Undertale = MediaPlayer.create(context, R.raw.undertale)
        Undertale?.isLooping = true

        val lstCanciones = resources.getStringArray(R.array.canciones)
        val adaptCanciones = ArrayAdapter(requireContext(), R.layout.spinner_color_selected, lstCanciones)
        adaptCanciones.setDropDownViewResource(R.layout.spinner_color_dropdown)
        spnCanciones.adapter = adaptCanciones
        spnCanciones.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cancionSelected = lstCanciones[p2]

                stopAndResetMediaPlayer(Home)
                stopAndResetMediaPlayer(Memory)
                stopAndResetMediaPlayer(Undertale)

                when(lstCanciones[p2]){
                    "Undertale OST - Home" -> startPlaying(Home)
                    "Undertale OST - Memory" -> startPlaying(Memory)
                    "Undertale OST - Undertale" -> startPlaying(Undertale)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        // volumen
        volumen.max = 100
        volumen.progress = 50

        volumen.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val volumenActual = p1 / 100.0f
                setVolumen(volumenActual)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun startPlaying(player: MediaPlayer?){
        player?.let{
            it.seekTo(0)
            it.start()

            // tiempo reproduccion
            reproduccion.max = it.duration

            // actualizar cada segundo
            handler.post(object: Runnable{
                override fun run() {
                    try {
                        reproduccion.progress = it.currentPosition
                        tiempo.text = "00:${reproduccion.progress/1000} de 00:${reproduccion.max/1000}"
                        if(it.isPlaying){
                            handler.postDelayed(this, 1000)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            reproduccion.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if(p2) {
                        it.seekTo(p1)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }
    }

    private fun stopAndResetMediaPlayer(player: MediaPlayer?){
        player?.let{
            if(it.isPlaying){
                it.stop()
                it.prepare()
            }
        }
    }

    private fun setVolumen(volumenActual: Float) {
        // establecer el mismo volumen a todas las canciones
        Home?.setVolume(volumenActual, volumenActual)
        Memory?.setVolume(volumenActual, volumenActual)
        Undertale?.setVolume(volumenActual, volumenActual)
    }

    override fun onPause() {
        super.onPause()

        stopAndResetMediaPlayer(Home)
        stopAndResetMediaPlayer(Memory)
        stopAndResetMediaPlayer(Undertale)
    }

    override fun onDestroy() {
        Home?.release()
        Memory?.release()
        Undertale?.release()
        super.onDestroy()
    }
}