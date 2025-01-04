package me.cerasi.objects

import me.cerasi.MultiBot
import it.auties.whatsapp.model.contact.Contact

class User(val id: String, val name: String, val service: Service, val provider: Any, val origin: Any?, multiBot: MultiBot) : MultiObject(multiBot) {
    fun whatsappOrigin() : Contact = origin as Contact
}