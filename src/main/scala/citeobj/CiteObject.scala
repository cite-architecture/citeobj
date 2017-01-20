package edu.holycross.shot.citeobj

import edu.holycross.shot.cite._


case class CiteObject(urn: Urn, properties: Vector[CiteProperty]) extends Citable {}
