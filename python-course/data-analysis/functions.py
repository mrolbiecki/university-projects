# Duplicated cote from the jupiter notebooks to be used in tests

import xml.etree.ElementTree as ET
import pandas as pd

# task 1
def create_general_information_dataframe(file_path):
    namespaces = {'drugbank': 'http://www.drugbank.ca'}
    data = []
    depth = 0

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if event == 'start':
            depth += 1
        elif event == 'end':
            depth -= 1

        if elem.tag == f"{{{namespaces['drugbank']}}}drug" and event == 'end' and depth == 1:
            drug_data = {}

            drugbank_id_elem = elem.find('drugbank:drugbank-id[@primary="true"]', namespaces)
            if drugbank_id_elem is not None:
                drug_data['drugbank-id'] = drugbank_id_elem.text

            for field in ['name', 'description', 'state', 'indication', 'mechanism-of-action']:
                field_elem = elem.find(f'drugbank:{field}', namespaces)
                if field_elem is not None:
                    drug_data[field] = field_elem.text

            drug_data['food-interactions'] = []

            for food_interaction in elem.findall('.//drugbank:food-interaction', namespaces):
                drug_data['food-interactions'].append(food_interaction.text)

            data.append(drug_data)
            elem.clear()

    return pd.DataFrame(data)

# task 2
def create_synonyms_dataframe(file_path):
    namespaces = {'drugbank': 'http://www.drugbank.ca'}
    data = []
    depth = 0

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if event == 'start':
            depth += 1
        elif event == 'end':
            depth -= 1

        if elem.tag == f"{{{namespaces['drugbank']}}}drug" and event == 'end' and depth == 1:
            drug_data = {}

            drugbank_id_elem = elem.find('drugbank:drugbank-id[@primary="true"]', namespaces)
            if drugbank_id_elem is not None:
                drug_data['drugbank-id'] = drugbank_id_elem.text

            synonyms_elem = elem.find('drugbank:synonyms', namespaces)
            if synonyms_elem is not None:
                synonyms = [synonym.text for synonym in synonyms_elem.findall('drugbank:synonym', namespaces)]
                drug_data['synonyms'] = synonyms

            data.append(drug_data)
            elem.clear()

    return pd.DataFrame(data)

# task 3
def create_products_dataframe(file_path):
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    data = []
    depth = 0

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if event == 'start':
            depth += 1
        elif event == 'end':
            depth -= 1

        if elem.tag == f"{{{namespace['drugbank']}}}drug" and event == 'end' and depth == 1:
            drugbank_id_elem = elem.find('drugbank:drugbank-id[@primary="true"]', namespace)
            if drugbank_id_elem is not None:
                drugbank_id = drugbank_id_elem.text
                products_elem = elem.find('drugbank:products', namespace)
                if products_elem is not None:
                    for product in products_elem.findall('drugbank:product', namespace):
                        product_dict = {'drugbank-id': drugbank_id}
                        for field in ['name', 'labeller', 'ndc-product-code', 'dosage-form', 'route', 'strength', 'country', 'source']:
                            field_elem = product.find(f'drugbank:{field}', namespace)
                            if field_elem is not None:
                                product_dict[field] = field_elem.text
                        data.append(product_dict)
            elem.clear()

    return pd.DataFrame(data)

# task 4
def create_pathways_dataframe(file_path):
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    data = []
    pathways = set()

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if elem.tag == f"{{{namespace['drugbank']}}}pathway" and event == 'end':
            id = elem.find('drugbank:smpdb-id', namespace).text
            if id not in pathways:
                pathways.add(id)
                pathway_dict = {}
                pathway_dict['id'] = elem.find('drugbank:smpdb-id', namespace).text
                pathway_dict['name'] = elem.find('drugbank:name', namespace).text
                pathway_dict['category'] = elem.find('drugbank:category', namespace).text
                drugs = elem.find('drugbank:drugs', namespace)
                pathway_dict['drugs'] = [drug.find('drugbank:drugbank-id', namespace).text for drug in drugs.findall('drugbank:drug', namespace)]
                enzymes = elem.find('drugbank:enzymes', namespace)
                pathway_dict['enzymes'] = [enzyme.text for enzyme in enzymes.findall('drugbank:uniprot-id', namespace)]
                data.append(pathway_dict)
            elem.clear()

    return pd.DataFrame(data)

# task 5
def create_pathways_interactions_dataframe(file_path):
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    data = []
    pathways = set()

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if elem.tag == f"{{{namespace['drugbank']}}}pathway" and event == 'end':
            id = elem.find('drugbank:smpdb-id', namespace).text
            if id not in pathways:
                pathways.add(id)
                pathway_dict = {}
                pathway_dict['name'] = elem.find('drugbank:name', namespace).text
                drugs = elem.find('drugbank:drugs', namespace)
                pathway_dict['drugs'] = [drug.find('drugbank:drugbank-id', namespace).text for drug in drugs.findall('drugbank:drug', namespace)]
                data.append(pathway_dict)
            elem.clear()

    return pd.DataFrame(data)

# task 6
def create_drug_pathways_dataframe(file_path):
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    data = []
    depth = 0

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if event == 'start':
            depth += 1
        elif event == 'end':
            depth -= 1

        if elem.tag == f"{{{namespace['drugbank']}}}drug" and event == 'end' and depth == 1:
            drugbank_id_elem = elem.find('drugbank:drugbank-id[@primary="true"]', namespace)
            drugbank_id = drugbank_id_elem.text
            pathways_count = len(elem.findall('.//drugbank:pathway', namespace))
            data.append({'drugbank-id': drugbank_id, 'pathways_count': pathways_count})
            elem.clear()

    return pd.DataFrame(data)

# task 7
def create_targets_dataframe(file_path):
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    data = []

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if elem.tag == f"{{{namespace['drugbank']}}}target" and event == 'end':
            target_dict = {}
            target_dict['id'] = elem.find('drugbank:id', namespace).text
            target_dict['name'] = elem.find('drugbank:name', namespace).text

            polypeptide = elem.find('drugbank:polypeptide', namespace)
            if polypeptide is not None:
                target_dict['source'] = polypeptide.get('source')
                target_dict['source-id'] = polypeptide.get('id')
                target_dict['gene-name'] = polypeptide.find('drugbank:gene-name', namespace).text
                target_dict['genatlas-id'] = None

                for external_identifier in polypeptide.findall('.//drugbank:external-identifier', namespace):
                    if external_identifier.find('drugbank:resource', namespace).text == 'GenAtlas':
                        target_dict['genatlas-id'] = external_identifier.find('drugbank:identifier', namespace).text

                target_dict['chromosome-location'] = polypeptide.find('drugbank:chromosome-location', namespace).text
                target_dict['cellular-location'] = polypeptide.find('drugbank:cellular-location', namespace).text
            data.append(target_dict)
            elem.clear()

    return pd.DataFrame(data)

# task 9
def create_drug_groups_dataframe(file_path):
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    group_dict = {}

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if elem.tag == f"{{{namespace['drugbank']}}}drug" and event == 'end':
            for group in elem.findall('.//drugbank:group', namespace):
                if group.text not in group_dict:
                    group_dict[group.text] = 1
                else:
                    group_dict[group.text] += 1
            elem.clear()

    return pd.DataFrame([group_dict])

# task 10
def create_drug_interactions_dataframe(file_path):
    namespace = {'drugbank': 'http://www.drugbank.ca'}
    data = []
    depth = 0

    for event, elem in ET.iterparse(file_path, events=('start', 'end')):
        if event == 'start':
            depth += 1
        elif event == 'end':
            depth -= 1

        if elem.tag == f"{{{namespace['drugbank']}}}drug" and event == 'end' and depth == 1:
            drugbank_id = elem.find("drugbank:drugbank-id[@primary='true']", namespace).text
            interactions = []
            for interaction in elem.findall('.//drugbank:drug-interaction', namespace):
                interactions.append(interaction.find('drugbank:drugbank-id', namespace).text)
            data.append({'drugbank-id': drugbank_id, 'interacts_with': interactions})
            elem.clear()

    return pd.DataFrame(data)

