package mx.tecnm.tepic.alramirezh.ladm_u4_practica1_contestadora

import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pantalla_contestadora.*
import kotlinx.android.synthetic.main.activity_pantalla_mis_contactos.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class PantallaContestadora : AppCompatActivity() {
    var fecha_a =  Calendar.getInstance()
    val baseDatos = FirebaseFirestore.getInstance()
    var listaTelBlancos = ArrayList<Llamada>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_contestadora)
        var hilo =  TesteoLlamadas(this)
        AlertDialog.Builder(this).setTitle("Mensaje")
            .setMessage("AL DAR CLIC EN OK COMENZARÁ EN FUNCIONAMIENTO LA AUTOCONTESTADORA.\n\nCADA 15s SE COMPROBARÁN LAS LLAMADAS PERDIDAS " +
                    "\n\nEN CASO DE HABER LLAMADAS PERDIDAS SE MOSTRARÁN EN ESTA PANTALLA \n\n" +
                    "NOTA:\n PARA DESACTIVAR LA AUTOCONTESTADORA DAR CLIC EN EL BOTON SUPERIOR MARCADO CON UNA X")
            .setPositiveButton("OK") { view, d ->
                view.dismiss()
                hilo.start()
            }.show()


        botonDesactivar.setOnClickListener {
            hilo.ejecutar = false
            finish()
        }


    }


    fun cargarLlamadasPerdidas():ArrayList<Llamada> {

        var resultado = ArrayList<Llamada>()
        try {
            val cursorLlamadas = contentResolver.query(
                Uri.parse("content://call_log/calls"),
                null, null, null, null)

            if (cursorLlamadas!!.moveToFirst()) {
                do {
                    var columna = cursorLlamadas.getColumnIndex("NUMBER")
                    var telefono = cursorLlamadas!!.getString(columna).replace(" ","").replace("-","")

                    var columna2 = cursorLlamadas.getColumnIndex("TYPE")
                    var tipo = cursorLlamadas.getInt(columna2)

                    var columna3 = cursorLlamadas.getColumnIndex("DATE")
                    var fecha = Calendar.getInstance()
                    fecha.timeInMillis = cursorLlamadas.getString(columna3).toLong()
                    var dif = fecha.compareTo(fecha_a)

                        if (tipo==3) {
                            if(dif>=0) {
                                resultado.add(Llamada(telefono,fecha))
                            }
                    }
                } while (cursorLlamadas.moveToNext())
                cursorLlamadas.close()
                return resultado
            }
        }catch (e: Exception){
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()

        }
        return resultado
    }








    fun addLlamada( llamada:Llamada ) {
        // LINEAR LAYOUT PRINCIPAL
        val contenedorMain = LinearLayout(this)
        runOnUiThread{
            contenedorLlamadas.addView(contenedorMain)
        }




        runOnUiThread {
            val p1 = contenedorMain.layoutParams as LinearLayout.LayoutParams
            p1.width = LinearLayout.LayoutParams.MATCH_PARENT
            p1.height = LinearLayout.LayoutParams.WRAP_CONTENT
            p1.bottomMargin = 12
            contenedorMain.layoutParams = p1

            contenedorMain.orientation = LinearLayout.VERTICAL
            contenedorMain.setPadding(3, 4, 0, 4)
            contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))

        }


       if (llamada.contacto!!.tipoLista == true) {
            //TRUE = LISTA BLANCA
            runOnUiThread {
                contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))
            }

        } else {
            //FALSO = LISTA NEGRA
            kotlin.run {
                contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
            }

        }


        //=================================================
        // LINEAR LAYOUT FECHA
        //=================================================
        val contenedorFecha = LinearLayout(this)
        runOnUiThread {
            contenedorMain.addView(contenedorFecha)
        }

        runOnUiThread {
            val p2 = contenedorFecha.layoutParams as LinearLayout.LayoutParams
            p2.width = LinearLayout.LayoutParams.MATCH_PARENT
            p2.height = LinearLayout.LayoutParams.WRAP_CONTENT
            p2.bottomMargin = 5
            contenedorFecha.layoutParams = p2
            contenedorFecha.orientation = LinearLayout.HORIZONTAL
        }


        //=================================================
        // IMAGE VIEW FECHA
        //=================================================
        val imagenFecha = ImageView(this)
        runOnUiThread {
            contenedorFecha.addView(imagenFecha)
        }

        runOnUiThread {
            val p3 = imagenFecha.layoutParams as LinearLayout.LayoutParams
            p3.width = LinearLayout.LayoutParams.WRAP_CONTENT
            p3.height = LinearLayout.LayoutParams.MATCH_PARENT
            p3.gravity = Gravity.CENTER_VERTICAL
            imagenFecha.layoutParams = p3
            imagenFecha.setImageResource(R.drawable.icono_calendario)
        }

        if (llamada.contacto!!.tipoLista== true) {
            runOnUiThread {
                imagenFecha.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))
                imagenFecha.setColorFilter(
                    ContextCompat.getColor(this, R.color.negroLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        } else {
            runOnUiThread {
                imagenFecha.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
                imagenFecha.setColorFilter(
                    ContextCompat.getColor(this, R.color.BlancoLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }

        //=================================================
        // TEXT VIEW ( FECHA )
        //=================================================
        val txtFecha = TextView(this)
        runOnUiThread{
            contenedorFecha.addView(txtFecha)
        }



        runOnUiThread {
            val p4 = txtFecha.layoutParams as LinearLayout.LayoutParams
            p4.width = LinearLayout.LayoutParams.MATCH_PARENT
            p4.height = LinearLayout.LayoutParams.WRAP_CONTENT
            p4.gravity = Gravity.CENTER
            txtFecha.layoutParams = p4

            txtFecha.setPadding(3, 0, 3, 3)
            txtFecha.setText(llamada.Fecha)
            txtFecha.textAlignment = View.TEXT_ALIGNMENT_CENTER
            txtFecha.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
            txtFecha.setTypeface(Typeface.DEFAULT_BOLD)
        }

        if (llamada.contacto!!.tipoLista == true) {
            runOnUiThread {
                txtFecha.setTextColor(ContextCompat.getColor(this, R.color.negroLigero))
            }
        } else {
            runOnUiThread {
                txtFecha.setTextColor(ContextCompat.getColor(this, R.color.BlancoLigero))
            }
        }
       // ToastPersonalizado(this,contenedorNombre.childCount.toString(),true).show()







        //=================================================
        // LINEAR LAYOUT TELEFONO
        //=================================================
        val contenedorTelefono = LinearLayout(this)
        runOnUiThread {
            contenedorMain.addView(contenedorTelefono)
        }



        runOnUiThread {
            val p5 = contenedorTelefono.layoutParams as LinearLayout.LayoutParams
            p5.width = LinearLayout.LayoutParams.MATCH_PARENT
            p5.height = LinearLayout.LayoutParams.WRAP_CONTENT
            contenedorTelefono.layoutParams = p5
            contenedorTelefono.orientation = LinearLayout.HORIZONTAL
        }


        //=================================================
        // IMAGE VIEW TELEFONO
        //=================================================
        val imagenTelefono = ImageView(this)
        runOnUiThread {
            contenedorTelefono.addView(imagenTelefono)
        }



        runOnUiThread {
            val p6 = imagenTelefono.layoutParams as LinearLayout.LayoutParams
            p6.width = LinearLayout.LayoutParams.WRAP_CONTENT
            p6.height = LinearLayout.LayoutParams.MATCH_PARENT
            p6.gravity = Gravity.CENTER_VERTICAL
            imagenTelefono.layoutParams = p6
            imagenTelefono.setImageResource(R.drawable.icono_telefono)
        }

        if (llamada.contacto!!.tipoLista == true) {
            runOnUiThread {
                imagenTelefono.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.BlancoLigero
                    )
                )
                imagenTelefono.setColorFilter(
                    ContextCompat.getColor(this, R.color.negroLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        } else {
            runOnUiThread {
                imagenTelefono.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
                imagenTelefono.setColorFilter(
                    ContextCompat.getColor(this, R.color.BlancoLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }


        //=================================================
        // TEXT VIEW ( TELEFONO )
        //=================================================
        val txtTelefono = TextView(this)
        runOnUiThread{
            contenedorTelefono.addView(txtTelefono)
        }



        runOnUiThread {
            val p7 = txtTelefono.layoutParams as LinearLayout.LayoutParams
            p7.width = LinearLayout.LayoutParams.MATCH_PARENT
            p7.height = LinearLayout.LayoutParams.WRAP_CONTENT
            p7.gravity = Gravity.CENTER
            txtTelefono.layoutParams = p7
            txtTelefono.textAlignment = View.TEXT_ALIGNMENT_CENTER
            txtTelefono.setPadding(3, 0, 3, 3)
            txtTelefono.setText(llamada.Telefono)
            txtTelefono.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
            txtTelefono.setTypeface(Typeface.DEFAULT_BOLD)
        }

        if (llamada.contacto!!.tipoLista == true) {
            runOnUiThread {


            txtTelefono.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        } else {
            runOnUiThread {
                txtTelefono.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        }

    }





    fun getContacto(llamada:Llamada) {
        //CONSULTAR LA INFORMACION DEL CONTACTO

        baseDatos.collection("contactos").whereEqualTo("TELEFONO",llamada.Telefono).get().addOnSuccessListener { documentos ->
            var i = documentos.size()
            var c = 0
            for (doc in documentos!!) {
                if (c == 0) {
                    var tipo = doc.getString("TIPOLISTA")
                    var tipoB = true
                    if (tipo == "Negra") {
                        tipoB = false
                    }
                    var contacto = Contacto(doc.getString("NOMBRE").toString(), llamada.Telefono, tipoB)
                    llamada.contacto = contacto
                    if(contacto.tipoLista==false){
                        //llamada perdida de un contacto de la lista negra

                        //consultar llamadas perdidas
                        baseDatos.collection("llamadasPerdidas").whereEqualTo("TELEFONO",llamada.Telefono).whereEqualTo("FECHA",llamada.Fecha)
                            .get().addOnSuccessListener { documentosllamadas ->
                                var total = documentosllamadas.size()
                                if(total == 0) { //SUBIR LLAMADA PERDIDA
                                    insertarLlamadaPerdida(llamada)
                                    envioSMS("NO DEVOLVERE TU LLAMADA, POR FAVOR NO INSISTAS",llamada.Telefono)
                                    ToastPersonalizado(this,"Mensaje SMS enviado a ${llamada.contacto!!.Nombre}",true).show()
                                }
                                else{
                                    addLlamada(llamada)

                                }



                            }.addOnFailureListener {
                                ToastPersonalizado(this, "NO SE PUDO ACCEDER A LLAMADAS PERDIDAS", true).show()
                            }

                    }else{
                        //LLAMADA PERDIDA DE UN CONTACTO DE LA LISTA BLANCA
                       agregarLlamadaBlanca(llamada)
                        addLlamada(llamada)

                    }
                    c++
                }
            }

        }.addOnFailureListener {
            ToastPersonalizado(this, "No se encontro un contacto", true).show()
        }
    }



    fun insertarLlamadaPerdida(llamada: Llamada){

            var datos = hashMapOf("NOMBRE" to llamada.contacto!!.Nombre, "TELEFONO" to llamada.contacto!!.Telefono, "FECHA" to llamada.Fecha )
            baseDatos.collection("llamadasPerdidas").add(datos as Any).addOnSuccessListener {
                addLlamada(llamada)
            }
                .addOnFailureListener {
                    ToastPersonalizado(this, "No se puedo guardar en la nube un contaqcto",true).show()
                    addLlamada(llamada)
                }

        }

    fun envioSMS(mensaje:String, telefono:String) {
        SmsManager.getDefault().sendTextMessage(telefono,null,mensaje,null,null)

    }

    fun agregarLlamadaBlanca(llamada:Llamada) {
        var encontrado = false
        listaTelBlancos.forEach{
            if(it.Telefono==llamada.Telefono && it.Fecha == llamada.Fecha){
                encontrado = true
            }
        }
        if(encontrado==false){
            listaTelBlancos.add(llamada)
            envioSMS("ESTOY OCUPADO, TE CONTACTARÉ MÁS TARDE",llamada.Telefono)
            ToastPersonalizado(this,"Mensaje SMS enviado a ${llamada.contacto!!.Nombre}",true).show()
        }
    }

}





class TesteoLlamadas(Activity:PantallaContestadora):Thread(){
    var puntero = Activity
    var ejecutar = true

    override fun run() {
        super.run()
        while(ejecutar){

            sleep(15000)
            puntero.runOnUiThread {
                puntero.contenedorLlamadas.removeAllViews()
            }
            var lista = puntero.cargarLlamadasPerdidas()
            var total = lista.size

            lista.forEach{
                puntero.getContacto(it)
            }

           puntero.runOnUiThread {
               ToastPersonalizado(puntero,"se encontraron ${total} llamadas perdidas",false).show()
           }

        }
    }
}