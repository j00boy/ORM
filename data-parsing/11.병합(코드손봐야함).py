import pandas as pd

# CSV 파일 불러오기
entrypoint_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/first_peak_data.csv'
final_entry_with_peak_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/all_peak_data.csv'

entrypoint_df = pd.read_csv(entrypoint_path)
final_entry_with_peak_df = pd.read_csv(final_entry_with_peak_path)

# entrypoint_df와 final_entry_with_peak_df를 id를 기준으로 병합
merged_df = pd.merge(entrypoint_df, final_entry_with_peak_df, on='id', how='left', suffixes=('', '_new'))

# 덮어쓰기 할 열 목록에서 'Elevation' 제외
columns_to_update = final_entry_with_peak_df.columns.difference(['id', 'Elevation'])

# final_entry_with_peak 데이터로 덮어쓰기
for column in columns_to_update:
    merged_df[column] = merged_df[column + '_new'].combine_first(merged_df[column])

# 불필요한 열 삭제
merged_df.drop(columns=[col + '_new' for col in columns_to_update], inplace=True)

# 업데이트된 데이터프레임을 새로운 CSV 파일로 저장
updated_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/release.csv'
merged_df.to_csv(updated_file_path, index=False, encoding='utf-8-sig')

