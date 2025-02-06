# Implement point 6 in such a way that it is possible to send the drug ID to your server, which will return the result in response.

from fastapi import FastAPI
from pydantic import BaseModel
import xml.etree.ElementTree as ET

app = FastAPI()


class DrugRequest(BaseModel):
    drug_id: str


def count_pathways(drug_id: str):
    file_path = 'drugbank_partial_and_generated.xml'
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    depth = 0

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if event == 'start':
            depth += 1
        elif event == 'end':
            depth -= 1

        if elem.tag == f"{{{namespace['drugbank']}}}drug" and event == 'end' and depth == 1:
            drugbank_id_elem = elem.find('drugbank:drugbank-id[@primary="true"]', namespace)
            drugbank_id = drugbank_id_elem.text if drugbank_id_elem is not None else None

            if drugbank_id == drug_id:
                pathways = elem.findall('.//drugbank:pathway', namespace)
                pathways_count = len(pathways)
                elem.clear()
                return pathways_count
            elem.clear()

    return 0


@app.post("/count_pathways/")
def get_pathways_count(request: DrugRequest):
    result = count_pathways(request.drug_id)
    return {"drug_id": request.drug_id, "pathways_count": result}