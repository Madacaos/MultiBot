package me.cerasi.objects

import me.cerasi.MultiBot

class User(val id: String, val name: String, val service: Service, val provider: Any, val origin: Any?, multiBot: MultiBot) : MultiObject(multiBot) {
    fun whatsappOrigin() : it.auties.whatsapp.model.contact.Contact = origin as it.auties.whatsapp.model.contact.Contact

}