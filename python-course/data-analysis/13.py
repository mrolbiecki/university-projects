# Create a simulator that generates a test database of 20,000 drugs.
# The values for the 19,900 generated drugs in the "DrugBank Id" column should have consecutive numbers,
# while the values in the other columns should be randomly selected from the values of 100 existing drugs.
# Save the results in the file drugbank_partial_and_generated.xml.

import xml.etree.ElementTree as ET
import random
import copy

input_file = 'drugbank_partial.xml'
output_file = 'drugbank_partial_and_generated.xml'

# Parse the input XML file
context = ET.iterparse(input_file, events=("start", "end"))
context = iter(context)
event, root = next(context)

# Determine the namespace if present
namespace = ''
if '}' in root.tag:
    namespace = root.tag.split('}')[0] + '}'

# Write the XML declaration and root element to the output file
with open(output_file, 'wb') as f:
    f.write(b'<?xml version="1.0" encoding="UTF-8"?>\n')
    f.write(b'<drugbank>\n')

depth = 0
tag_structure = {}

# Process each element in the input XML file
with open(output_file, 'ab') as f:
    for event, elem in context:
        if event == "start" and elem.tag == f"{namespace}drug":
            depth += 1
        elif event == "end" and elem.tag == f"{namespace}drug":
            depth -= 1

        if event == "end" and elem.tag == f"{namespace}drug" and depth == 0:
            print(f'Processing drug: {elem.find(f"{namespace}drugbank-id", {"primary": "true"}).text}')
            for child in elem:
                tag = child.tag
                if tag == f"{namespace}drugbank-id":
                    continue
                if tag not in tag_structure:
                    tag_structure[tag] = {'elements': []}
                tag_structure[tag]['elements'].append(copy.deepcopy(child))

            # Write the processed drug element to the output file
            f.write(ET.tostring(elem, encoding='utf-8'))
            f.write(b'\n')
            root.clear()

print("Collected tag structure:", {k: len(v['elements']) for k, v in tag_structure.items()})

# Function to generate a new drug element
def generate_new_drug(tag_structure, new_id):
    new_drug = ET.Element(f'{namespace}drug')
    drugbank_id = ET.SubElement(new_drug, f'{namespace}drugbank-id', {'primary': 'true'})
    drugbank_id.text = f'DB{new_id:05d}'

    for tag, details in tag_structure.items():
        chosen_element = copy.deepcopy(random.choice(details['elements']))
        new_drug.append(chosen_element)

    return new_drug

# Generate and write 1900 new drug elements to the output file
with open(output_file, 'ab') as f:
    for i in range(1, 19901):
        new_drug = generate_new_drug(tag_structure, i + 108)
        f.write(ET.tostring(new_drug, encoding='utf-8'))
        f.write(b'\n')

# Close the root element in the output file
with open(output_file, 'ab') as f:
    f.write(b'</drugbank>\n')

print(f'Original drugs and 1900 new drugs saved to {output_file}')