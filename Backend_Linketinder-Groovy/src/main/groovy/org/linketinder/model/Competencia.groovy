package org.linketinder.model

import groovy.transform.Canonical

@Canonical
class Competencia {
    Integer id
    String competencia

    @Override
    String toString() {"$id - $competencia"}
}
