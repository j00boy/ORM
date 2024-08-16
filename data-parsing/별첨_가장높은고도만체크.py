import pandas as pd

# CSV 파일 경로 설정
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/multiple_peak_with_elevation.csv'

# CSV 파일 불러오기
data = pd.read_csv(file_path)

# 각 MNTN_CODE에 대해 Elevation이 가장 높은 행 추출
highest_elevation_data = data.loc[data.groupby('MNTN_CODE')['Elevation'].idxmax()]

# 추출된 데이터를 새로운 CSV 파일로 저장
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/highest_elevation_data.csv'
highest_elevation_data.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Highest elevation data saved to {output_file_path}")
