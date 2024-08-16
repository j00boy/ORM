import pandas as pd

# Load the CSV file
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/startAndPeak.csv'
data = pd.read_csv(file_path)

# Extract rows where 'Entry_Point' is '정상'
data_normal = data[data['Entry_Point'] == '정상']

# Identify MNTN_CODEs with multiple '정상' entries
mntn_codes_multiple_normal = data_normal['MNTN_CODE'].value_counts()
mntn_codes_multiple_normal = mntn_codes_multiple_normal[mntn_codes_multiple_normal > 1].index

# Filter data to include only rows with these MNTN_CODEs
filtered_data = data[data['MNTN_CODE'].isin(mntn_codes_multiple_normal)]

# Save the filtered data to a new CSV file
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/if_multiple_peak.csv'
filtered_data.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Filtered data saved to {output_file_path}")
