import argparse
from functools import reduce
from pathlib import Path
import json

def parse_args():
    parser = argparse.ArgumentParser(description="Convert Jupyter Notebook to Python script.")
    parser.add_argument('path', type=Path, help="Path to the notebook file")
    args = parser.parse_args()
    if not args.path.exists() or not args.path.is_file():
        parser.error(f"The path '{args.path}' does not exist or is not a file.")
    return args.path

def format_cell(cell):
    prefix = '' if cell['cell_type'] == 'code' else '#'
    content = prefix.join(cell['source']).rstrip()
    return f"{prefix}{content}\n\n"

def convert_notebook_to_script(file_path):
    data = json.loads(path.read_text())
    code = list(map(format_cell, data.get('cells', [])))
    file_path.with_suffix('.py').write_text(''.join(code))
    exercises_cnt = reduce(lambda x, y: x + y.count('# Ćwiczenie'), code, 0)
    print('Liczba ćwiczeń: ', exercises_cnt)

if __name__ == "__main__":
    path = parse_args()
    convert_notebook_to_script(path)
