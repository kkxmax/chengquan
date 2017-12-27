//
//  ProductTableViewCell.h
//  chengxin
//
//  Created by common on 7/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "ItemTableViewCell.h"

@interface ProductTableViewCell : UITableViewCell

@property id<ItemTableViewCellDelegate> delegate;
@property (nonatomic, retain) IBOutlet UIImageView* photo;
@property (nonatomic, retain) IBOutlet UILabel* name;
@property (nonatomic, retain) IBOutlet UILabel* price;
@property (nonatomic, retain) IBOutlet UILabel* status;
@property (nonatomic, retain) IBOutlet UIButton* btnEdit;
@property (nonatomic, retain) IBOutlet UIButton* btnDelete;
@property (nonatomic, retain) IBOutlet UIButton* btnUpDown;

@property NSNumber* productID;
@property (nonatomic) BOOL isUp;

-(IBAction)onEdit:(id)sender;
-(IBAction)onDelete:(id)sender;
-(IBAction)onUpDown:(id)sender;
-(void)setIsUp:(BOOL)isUp;
@end
