#!/bin/bash

set -o errexit
set -o nounset
set -o pipefail

script_path=$(dirname "$(readlink -f "$0")")
base_dir="$script_path"
oft_script="$base_dir/oft"
report_file=$base_dir/target/self-trace-report.html

mkdir -p $(dirname "$report_file")

$oft_script trace \
    --output-format html -f "$report_file" \
    "$base_dir/doc/spec" \
    "$base_dir/importer/markdown/src" \
    "$base_dir/importer/specobject/src" \
    "$base_dir/importer/zip/src" \
    "$base_dir/importer/tag/src" \
    "$base_dir/core/src/main" \
    "$base_dir/core/src/test/java" \
    "$base_dir/reporter/plaintext/src" \
    "$base_dir/reporter/html/src" \
    "$base_dir/reporter/aspec/src" \
    "$base_dir/product/src/test/java" \
    "$base_dir/api/src" \
    "$base_dir/exporter/specobject/src" \
    "$base_dir/exporter/common/src" \
    "$base_dir/testutil/src"
