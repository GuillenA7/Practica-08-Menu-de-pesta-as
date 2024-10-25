package com.example.prctica08_mendepestaas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast

class FormularioFragment : Fragment() {
    private lateinit var view: View
    private lateinit var estrellas: RatingBar
    private lateinit var enviar: Button
    private var numEstrellas: Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_formulario, container, false)

        // Asociar componentes
        estrellas = view.findViewById(R.id.rbEstrellas)
        enviar = view.findViewById(R.id.btnEnviar)

        // Configurar RatingBar
        estrellas.numStars = 5
        estrellas.setOnRatingBarChangeListener { _, rating, _ ->
            numEstrellas = rating
        }

        // Evento del botón Enviar
        enviar.setOnClickListener {
            val servicio = when {
                numEstrellas < 2.0 -> "Mal servicio"
                numEstrellas in 2.0..4.0 -> "Buen servicio"
                else -> "Excelente servicio"
            }

            // Mostrar mensaje con la evaluación del servicio
            Toast.makeText(
                context,
                "Evaluaste el servicio con $numEstrellas estrellas. $servicio",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }
}