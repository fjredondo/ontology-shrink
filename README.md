# ontology-shrink
Command line java application for ontology that reduce the human readable content from ontology by a percentage.
 
Particularly, this application is able to remove a percentage of names, synonyms and descriptions (types of AnnotationAssertionAxioms) from the ontology. It generates a new ontology with less human readable content.

### Identified annotations properties for describing names, synonyms, and descriptions
- *Name*
    - http://www.w3.org/2004/02/skos/core#prefLabel
    - http://www.w3.org/2000/01/rdf-schema#label
    - http://schema.org/name
    - http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#P108
    - http://purl.obolibrary.org/obo/IAO_0000589
    - http://purl.obolibrary.org/obo/IAO_0000111
    - http://xmlns.com/foaf/0.1/name
- *Synonym*
    - http://www.w3.org/2004/02/skos/core#altLabel
    - http://www.geneontology.org/formats/oboInOwl#hasExactSynonym
    - http://www.geneontology.org/formats/oboInOwl#hasRelatedSynonym
    - http://www.geneontology.org/formats/oboInOwl#hasBroadSynonym
    - http://www.geneontology.org/formats/oboInOwl#hasNarrowSynonym
    - http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#P90
    - http://purl.obolibrary.org/obo/IAO_0000118
    - http://purl.obolibrary.org/obo/OBI_9991119
    - http://purl.obolibrary.org/obo/OBI_9991118
    - http://purl.obolibrary.org/obo/OBI_0001847
    - http://purl.obolibrary.org/obo/OBI_0001886
- *Description*
    - http://purl.obolibrary.org/obo/IAO_0000115
    - http://www.w3.org/2004/02/skos/core#definition
    - http://www.w3.org/2000/01/rdf-schema#comment
    - http://purl.org/dc/elements/1.1/description
    - http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#P97

# Usage
## Command
`java -jar ontology-shrink.jar -i <input> -r <HRA_reduction>`

Where

- **input** is an owl ontology path.
- **HRA_reduction** is the Percentage of human readable annotation reduction.

## Dependencies
If you want to test, modify or compile the application from the source code, you will need to download the following libraries that are used by the application:

- **commons-cli-1.4.jar**, used for parsing the command line arguments. [Link](https://commons.apache.org/proper/commons-cli/).
- **ontoenrich-core-2.0.0.jar**, used to perform operations on the ontologies. The source code of this library is not available yet, but it is supported by several publications:
    -  [https://link.springer.com/chapter/10.1007/978-3-319-17966-7_25](https://link.springer.com/chapter/10.1007/978-3-319-17966-7_25)
    - [https://hal.archives-ouvertes.fr/hal-03155057/](https://hal.archives-ouvertes.fr/hal-03155057/)

You can download these libraries from [here](http://semantics.inf.um.es/ontology-metrics-libs/libs.zip).
