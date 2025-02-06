# Unit tests for functions.py

import pytest
import pandas as pd
from functions import (
    create_general_information_dataframe,
    create_synonyms_dataframe,
    create_products_dataframe,
    create_pathways_dataframe,
    create_pathways_interactions_dataframe,
    create_drug_pathways_dataframe,
    create_targets_dataframe,
    create_drug_groups_dataframe,
    create_drug_interactions_dataframe
)

@pytest.fixture
def file_path():
    return 'test_input.xml'

# task 1
def test_create_general_information_dataframe(file_path):
    df = create_general_information_dataframe(file_path)
    expected_data = [{
        'drugbank-id': 'DB00001',
        'name': 'drug1',
        'description': 'description1',
        'state': 'state1', 'indication': 'indication1',
        'mechanism-of-action': 'mechanism1',
        'food-interactions': ['food1']
    }]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# task 2
def test_create_synonyms_dataframe(file_path):
    df = create_synonyms_dataframe(file_path)
    expected_data = [
        {'drugbank-id': 'DB00001', 'synonyms': ['synonym1', 'synonym2']}
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# task 3
def test_create_products_dataframe(file_path):
    df = create_products_dataframe(file_path)
    expected_data = [
        {
            'drugbank-id': 'DB00001',
            'name': 'product1',
            'labeller': 'labeller1',
            'ndc-product-code': 'ndc-product-code1',
            'dosage-form': 'dosage-form1',
            'route': 'route1',
            'strength': 'strength1',
            'country': 'country1',
            'source': 'source1'
        },
        {
            'drugbank-id': 'DB00001',
            'name': 'product2',
            'labeller': 'labeller2',
            'ndc-product-code': 'ndc-product-code2',
            'dosage-form': 'dosage-form2',
            'route': 'route2',
            'strength': 'strength2',
            'country': 'country2',
            'source': 'source2'
        }
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# task 4
def test_create_pathways_dataframe(file_path):
    df = create_pathways_dataframe(file_path)
    expected_data = [
        {
            'id': 'PW00001',
            'name': 'pathway1',
            'category': 'category1',
            'drugs': ['DB00001', 'DB00002'],
            'enzymes': ['uniprot1', 'uniprot2']
        }
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# task 5
def test_create_pathways_interactions_dataframe(file_path):
    df = create_pathways_interactions_dataframe(file_path)
    expected_data = [
        {
            'name': 'pathway1',
            'drugs': ['DB00001', 'DB00002']
        }
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# task 6
def test_create_drug_pathways_dataframe(file_path):
    df = create_drug_pathways_dataframe(file_path)
    expected_data = [
        {
            'drugbank-id': 'DB00001',
            'pathways_count': 1
        }
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# task 7
def test_create_targets_dataframe(file_path):
    df = create_targets_dataframe(file_path)
    expected_data = [
        {
            'id': 'target-id1',
            'name': 'target1',
            'source': 'source1',
            'source-id': 'source-id1',
            'gene-name': 'gene-name1',
            'genatlas-id': 'genatlas-id1',
            'chromosome-location': 'chromosome-location1',
            'cellular-location': 'cellular-location1',
        }
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# task 9
def test_create_drug_groups_dataframe(file_path):
    df = create_drug_groups_dataframe(file_path)
    expected_data = [
        {
            'withdrawn': 1,
            'approved': 1
        }
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)


# task 10
def test_create_drug_interactions_dataframe(file_path):
    df = create_drug_interactions_dataframe(file_path)
    expected_data = [
        {
            'drugbank-id': 'DB00001',
            'interacts_with': ['DB00002', 'DB00003']
        }
    ]
    expected_df = pd.DataFrame(expected_data)
    pd.testing.assert_frame_equal(df, expected_df)

# python3 -m pytest