package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CitePropertyDef(urn: Cite2Urn, label: String, propertyType: CitePropertyType, vocabularyList: Vector[String] = Vector.empty) {

  propertyType match {
    case ControlledVocabType => assert (vocabularyList.nonEmpty)
    case _ => assert(vocabularyList.isEmpty)
  }
}
