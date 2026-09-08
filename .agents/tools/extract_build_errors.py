#!/usr/bin/env python3
"""
extract_build_errors.py - Error log parser for AI Self-Correction Loop

Parses Gradle stdout/stderr or test log files and extracts concise, surgical
error diagnostics (file path, line number, compilation error, assertion failure).

Usage:
  python3 .agents/tools/extract_build_errors.py [log_file]
  ./gradlew :shared:allTests 2>&1 | python3 .agents/tools/extract_build_errors.py
"""

import sys
import re
import pathlib

def parse_errors(log_text: str):
    compiler_errors = []
    test_failures = []
    gradle_exceptions = []

    # Regex patterns
    kt_comp_pat = re.compile(r'e:\s+(/[^:]+\.kt):\s*\((\d+),\s*(\d+)\):\s*(.+)')
    test_fail_pat = re.compile(r'(com\.iqbalwork\.robithoh\.[^\s>]+)\s*>\s*([^\s]+)\s+FAILED')
    assertion_pat = re.compile(r'(Expected\s+<[^>]+>\s+but\s+was\s+<[^>]+>|AssertionError:.+|java\.lang\.[A-Za-z]+Exception:.+)')

    lines = log_text.split('\n')

    for line in lines:
        # Match Kotlin compilation error
        m_kt = kt_comp_pat.search(line)
        if m_kt:
            filepath, row, col, msg = m_kt.groups()
            compiler_errors.append({
                'file': filepath,
                'line': int(row),
                'col': int(col),
                'message': msg.strip()
            })
            continue

        # Match Test failure name
        m_test = test_fail_pat.search(line)
        if m_test:
            cls_name, test_method = m_test.groups()
            test_failures.append({
                'class': cls_name,
                'method': test_method
            })
            continue

        # Match Gradle exception / failure cause
        if 'BUILD FAILED' in line or 'Where:' in line or '* What went wrong:' in line:
            gradle_exceptions.append(line.strip())

    return {
        'compiler_errors': compiler_errors,
        'test_failures': test_failures,
        'gradle_exceptions': gradle_exceptions
    }

def main():
    if len(sys.argv) > 1 and sys.argv[1] != '-':
        log_file = pathlib.Path(sys.argv[1])
        if not log_file.exists():
            print(f"Error: Log file not found: {log_file}", file=sys.stderr)
            sys.exit(1)
        raw_text = log_file.read_text(encoding='utf-8', errors='ignore')
    else:
        raw_text = sys.stdin.read()

    result = parse_errors(raw_text)

    print("========================================================")
    print("🔍 AI Self-Correction Diagnostic Summary")
    print("========================================================")

    if not result['compiler_errors'] and not result['test_failures'] and not result['gradle_exceptions']:
        print("✅ No specific compilation or test failure patterns detected in input.")
        return

    if result['compiler_errors']:
        print(f"\n❌ Compilation Errors ({len(result['compiler_errors'])}):")
        for err in result['compiler_errors']:
            print(f"  • {err['file']}:{err['line']}:{err['col']} -> {err['message']}")

    if result['test_failures']:
        print(f"\n🧪 Failed Unit Tests ({len(result['test_failures'])}):")
        for tf in result['test_failures']:
            print(f"  • {tf['class']} > {tf['method']}")

    if result['gradle_exceptions']:
        print("\n⚙️ Gradle Execution Context:")
        for ge in result['gradle_exceptions']:
            print(f"  • {ge}")

    print("========================================================")

if __name__ == '__main__':
    main()
