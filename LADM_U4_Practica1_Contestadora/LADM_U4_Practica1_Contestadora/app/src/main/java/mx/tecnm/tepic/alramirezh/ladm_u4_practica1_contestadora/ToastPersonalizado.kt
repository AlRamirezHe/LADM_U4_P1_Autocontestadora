package mx.tecnm.tepic.alramirezh.ladm_u4_practica1_contestadora

import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toast.*
import kotlinx.android.synthetic.main.toast.view.*

class ToastPersonalizado(activity: AppCompatActivity, mensaje: String, largo:Boolean ) {
    val mensaje = mensaje
    val a = activity
    val largo = largo
    var duracion = 0

    fun show(){
        try {
            if(largo){
                duracion = Toast.LENGTH_LONG
            }else{
                duracion = Toast.LENGTH_SHORT
            }
            val layout = a.layoutInflater.inflate( R.layout.toast, a.contenedorToast )
            layout.txtToast.setText(mensaje)
            Toast(a).apply {
                duration = duracion
                setGravity(Gravity.BOTTOM, 0, 18)
                view = layout
            }.show()
        }catch(e:Exception){ Toast.makeText( a , e.message , Toast.LENGTH_LONG).show() }
    }
}