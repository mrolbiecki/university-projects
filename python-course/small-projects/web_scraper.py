import requests
from bs4 import BeautifulSoup
import pandas
import random

# Pobieram aktualności zamiast kalentarza wydarzeń, ponieważ kalendarz jest ładowany dynamicznie

# URL of the website to scrape
url = "https://www.mimuw.edu.pl"

response = requests.get(url)
html_content = response.text
soup = BeautifulSoup(html_content, 'html.parser')

# Find the container with news items
news_container = soup.find('div', class_='info-grid news-grid')

news_data = []

if news_container:
    # Find all individual news items
    news_items = news_container.find_all('div', class_='info-item news-item news-filter-others')

    for news in news_items:
        date = news.find('div', class_='info-item-text-date').get_text(strip=True)
        title = news.find('h3').get_text(strip=True)
        link_tag = news.find('span', class_='more').find('a')
        link = link_tag['href'] if link_tag else None

        # Append the extracted details to the news_data list as a dictionary
        news_data.append({
            'title': title,
            'link': link,
            'date': date
        })
else:
    print("No news container found on the page.")

# Convert the dictionary to a CSV file using pandas
df = pandas.DataFrame(news_data)
df.to_csv('news_data.csv', index=False)

# Print 5 distinct events from dictionary
for i in range(5):
    print(random.choice(news_data))
