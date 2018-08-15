package com.github.servb.pph.pheroes.common.event

class iTimeEventMgr {
    fun EventsCount() = m_events.size
    fun Event(idx: Int) = m_events[idx]
    fun AddEvent(event: iTimeEvent) = m_events.add(event)
    fun DeleteEvent(idx: Int) {
        m_events.removeAt(idx)
    }

    fun DeleteAll() = m_events.clear()

    private val m_events = mutableListOf<iTimeEvent>()
}
