package mx.tecnm.tepic.alramirezh.ladm_u4_practica1_contestadora

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val baseDatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)






        itemVerContactosBD.setOnClickListener {
            val lanzar = Intent(this@MainActivity, PantallaContactosBD::class.java)
            startActivity(lanzar)
        }

        itemVerMisContactos.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    10
                )
            } else {
                val lanzar = Intent(this@MainActivity, PantallaMisContactos::class.java)
                startActivity(lanzar)
            }

        }

        itemBotonContestadora.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_CALL_LOG
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_CALL_LOG),
                    15
                )
            } else {
                if (ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.SEND_SMS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.SEND_SMS),
                        20
                    )
                } else {
                    val lanzar = Intent(this@MainActivity, PantallaContestadora::class.java)
                    startActivity(lanzar)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==10){
            if( ActivityCompat.checkSelfPermission( this ,
                    android.Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED )
            {
                ToastPersonalizado(this,"Debes aceptar permiso para ver lista de contactos",false).show()
            }else {
                val lanzar = Intent(this@MainActivity, PantallaMisContactos::class.java)
                startActivity(lanzar)
            }
        }
        else if(requestCode==15){
            if( ActivityCompat.checkSelfPermission( this ,
                    android.Manifest.permission.READ_CALL_LOG ) != PackageManager.PERMISSION_GRANTED )
            {
                ToastPersonalizado(this,"Debes aceptar permiso para ver registro de llamadas",false).show()
            }else {
                val lanzar = Intent(this@MainActivity, PantallaContestadora::class.java)
                startActivity(lanzar)
            }
        }
        else if(requestCode==20){
            if( ActivityCompat.checkSelfPermission( this ,
                    android.Manifest.permission.SEND_SMS ) != PackageManager.PERMISSION_GRANTED )
            {
                ToastPersonalizado(this,"Debes aceptar permiso para enviar mensajes",false).show()
            }else {
                val lanzar = Intent(this@MainActivity, PantallaContestadora::class.java)
                startActivity(lanzar)
            }
        }




    }
}
    
