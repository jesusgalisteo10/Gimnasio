package com.example.view;

import com.example.App;



public abstract class Controller {
    /**
     * Representa una clase base abstracta para los controladores en la aplicación.
     * Define métodos abstractos para manejar eventos de apertura y cierre.
     * Proporciona un metodo para establecer la instancia de la aplicación.
     */
    App app;

    /**
     * Establece la instancia de la aplicación principal.
     *
     * @param app la instancia de la aplicación principal
     */
    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Define el metodo a implementar para manejar el evento de apertura.
     *
     * @param input los datos de entrada para el evento de apertura
     * @throws Exception si hay un problema durante el manejo del evento
     */
    public abstract void onOpen(Object input) throws Exception;

    /**
     * Define el metodo a implementar para manejar el evento de cierre.
     *
     * @param output los datos de salida para el evento de cierre
     */
    public abstract void onClose(Object output);

}


