package com.dtp.annotations

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class ChamberTable(val overridesBuilder: Boolean = false)