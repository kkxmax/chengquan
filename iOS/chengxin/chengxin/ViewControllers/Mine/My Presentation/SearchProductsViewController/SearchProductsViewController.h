//
//  SearchProductsViewController.h
//  chengxin
//
//  Created by common on 7/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ProductTableViewCell.h"
#import "ItemTableViewCell.h"

typedef enum {
    em_Product,
    em_Item,
    em_Service
} SearchProductTab;
@interface SearchProductsViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate, ItemTableViewCellDelegate, UIAlertViewDelegate>
{
}

@property (nonatomic) SearchProductTab selected_tab;
@property (nonatomic, retain) IBOutlet UITableView* tableView;
@property (nonatomic, retain) IBOutlet UIButton* btnProduct;
@property (nonatomic, retain) IBOutlet UIButton* btnItem;
@property (nonatomic, retain) IBOutlet UIButton* btnService;
@property (nonatomic, retain) IBOutlet UILabel* lblProductUnderline;
@property (nonatomic, retain) IBOutlet UILabel* lblItemUnderline;
@property (nonatomic, retain) IBOutlet UILabel* lblServiceUnderline;
@property (nonatomic, retain) IBOutlet UISearchBar* searchBar;
@property (nonatomic, retain) IBOutlet UIButton* btnKeyboardReturn;
@property (nonatomic, retain) IBOutlet UIButton* btnWrite;
@property (nonatomic, retain) IBOutlet UIView *blankView;

- (IBAction)onProduct:(id)sender;
- (IBAction)onItem:(id)sender;
- (IBAction)onService:(id)sender;
- (IBAction)onBack:(id)sender;
- (IBAction)onKeyboardReturn:(id)sender;
- (IBAction)onWrite:(id)sender;
@end
