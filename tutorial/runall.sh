tests=("0-a-setup" "1-a-add" "1-b-sub" "1-c-mul" "1-d-div" "1-e-expressions" "2-a-type-conversions-up" "2-b-type-conversions-down" "3-a-array-indexing" "3-b-structure-access" "4-boolean-relational-expressions" "5-a-if-else" "5-b-loops" "5-c-calls" "6-bitwise-expressions")

sum=0

# Loop through each element
for test in "${tests[@]}"; do
	echo $test
	cd $test
	result=$(sh make.sh)
	sum=$((sum + result))
	cd ..
done



echo failures = $sum
