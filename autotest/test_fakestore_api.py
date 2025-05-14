import requests
import json
from typing import List, Dict, Any

class ProductValidator:
    def __init__(self):
        self.api_url = "https://fakestoreapi.com/products"
        self.defects: List[Dict[str, Any]] = []

    def validate_product(self, product: Dict[str, Any]) -> None:
        """
        Validate a single product against all rules.
        Adds any found defects to self.defects list.
        """
        product_id = product.get('id', 'Unknown ID')

        # Validate title
        title = product.get('title', '')
        if not isinstance(title, str) or not title.strip():
            self.defects.append({
                'id': product_id,
                'defect_type': 'Empty title',
                'details': f"Product title is empty or invalid: '{title}'"
            })

        # Validate price
        price = product.get('price', 0)
        if not isinstance(price, (int, float)) or price < 0:
            self.defects.append({
                'id': product_id,
                'defect_type': 'Negative or invalid price',
                'details': f"Product price is negative or invalid: {price}"
            })

        # Validate rating
        rating = product.get('rating', {})
        if isinstance(rating, dict):
            rate = rating.get('rate', 0)
            if not isinstance(rate, (int, float)) or rate > 5:
                self.defects.append({
                    'id': product_id,
                    'defect_type': 'Invalid rating',
                    'details': f"Product rating exceeds 5 or is invalid: {rate}"
                })
        else:
            self.defects.append({
                'id': product_id,
                'defect_type': 'Missing rating',
                'details': "Product rating object is missing or invalid"
            })

    def run_validation(self) -> None:
        """
        Run the validation process and print results.
        """
        try:
            # Make API request
            response = requests.get(self.api_url)
            
            # Validate HTTP status code
            if response.status_code != 200:
                print(f"❌ API request failed with status code: {response.status_code}")
                return

            print("✅ API request successful (Status Code: 200)")
            
            # Parse and validate products
            products = response.json()
            if not isinstance(products, list):
                print("❌ API response is not a list of products")
                return

            print(f"\nValidating {len(products)} products...")
            for product in products:
                self.validate_product(product)

            # Print validation results
            if not self.defects:
                print("\n✅ No defects found! All products passed validation.")
            else:
                print(f"\n❌ Found {len(self.defects)} defects:")
                for defect in self.defects:
                    print(f"\nProduct ID: {defect['id']}")
                    print(f"Defect Type: {defect['defect_type']}")
                    print(f"Details: {defect['details']}")

        except requests.RequestException as e:
            print(f"❌ Error making API request: {e}")
        except json.JSONDecodeError as e:
            print(f"❌ Error parsing API response: {e}")
        except Exception as e:
            print(f"❌ Unexpected error: {e}")

def main():
    print("Starting Fake Store API validation...\n")
    validator = ProductValidator()
    validator.run_validation()

if __name__ == "__main__":
    main() 