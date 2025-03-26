package com.electrosim.simulation

import android.graphics.Bitmap
import com.electrosim.Container
import com.electrosim.Element
import com.electrosim.ElementInterface

abstract class SimElement(container: Container) : Element(container) {

    val inputPorts: MutableList<InputPort> = ArrayList()
    val outputPorts: MutableList<OutputPort> = ArrayList()

    protected fun addInputPort(i: InputPort) {
        inputPorts.add(i)
        addLinkPoint(i)
    }
    
    protected fun addOutputPort(o: OutputPort) {
        outputPorts.add(o)
        addLinkPoint(o)
    }
    
    fun run() {
        // Empty implementation
    }

    protected abstract fun simulate()
    
    protected open fun prepareToStart() {
        for (ip in inputPorts) {
            ip.prepareToStart()
        }
        
        for (op in outputPorts) {
            op.prepareToStart()
        }
    }
    
    protected open fun prepareToStop() {
        for (ip in inputPorts) {
            ip.prepareToStop()
        }
        
        for (op in outputPorts) {
            op.prepareToStop()
        }
    }
} 