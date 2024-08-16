import pandas as pd

# CSV 파일 경로 설정
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/if_multiple_peak.csv'

# CSV 파일 불러오기
all_peak_data = pd.read_csv(file_path)

# 'Entry_Point'가 '정상'인 행 추출
data_entry_point_normal = all_peak_data[all_peak_data['Entry_Point'] == '정상']

# 추출된 데이터를 새로운 CSV 파일로 저장
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/entry_point_peak.csv'
data_entry_point_normal.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Filtered data saved to {output_file_path}")
