# DrugBank Data Analysis Project

This project is focused on analyzing a partial version of the DrugBank database, which contains information on drugs and their interactions. The dataset is not included, but can be accessed at https://www.drugbank.com/. Various data analysis tasks and visualizations are performed to summarize and explore the dataset.

## Key Features
- **Drug Data Frame**: Creates a dataframe with essential drug information such as ID, name, type, description, dosage form, indications, mechanism of action, and food interactions.
- **Synonym Search & Visualization**: Allows searching for synonyms of drugs and visualizes the synonym graph using NetworkX.
- **Pharmaceutical Product Information**: Builds a dataframe showing products containing specific drugs, with details like producer, dosage form, and registration information.
- **Pathway Interactions**: Identifies signaling and metabolic pathways interacting with drugs and visualizes the interactions.
- **Protein Targets**: Collects data on proteins targeted by drugs and creates visualizations of target distribution in the cell.
- **Drug Approval Status**: Displays drug approval statuses and provides a chart of approved, experimental, and withdrawn drugs.
- **Drug-Drug Interactions**: Collects and displays potential interactions between different drugs.
- **Custom Data Visualizations**: Offers a customized graphical presentation based on the dataset, such as interactions with specific genes.
- **Generated Drug Database**: Simulates a larger database with 20,000 drug entries and conducts similar analyses.
- **Unit Testing**: Unit tests are included to ensure the correctness of the analysis and functionality.
