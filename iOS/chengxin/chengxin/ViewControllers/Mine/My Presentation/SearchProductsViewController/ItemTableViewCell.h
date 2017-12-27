//
//  ItemTableViewCell.h
//  chengxin
//
//  Created by common on 7/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>


@class ItemTableViewCell;

@protocol ItemTableViewCellDelegate
-(void)itemEditing:(UITableViewCell*)cell;
-(void)itemDeleting:(UITableViewCell*)cell;
-(void)itemUpDown:(UITableViewCell*)cell;
@end

@interface ItemTableViewCell : UITableViewCell

@property id<ItemTableViewCellDelegate> delegate;
@property (nonatomic, retain) IBOutlet UIImageView* photo;
@property (nonatomic, retain) IBOutlet UILabel* name;
@property (nonatomic, retain) IBOutlet UITextView* comment;
@property (nonatomic, retain) IBOutlet UILabel* status;
@property (nonatomic, retain) IBOutlet UIButton* business;
@property (nonatomic, retain) IBOutlet UIButton* btnEdit;
@property (nonatomic, retain) IBOutlet UIButton* btnDelete;
@property (nonatomic, retain) IBOutlet UIButton* btnUpDown;
@property  NSNumber* itemID;
@property (nonatomic) BOOL isUp;

-(IBAction)onEdit:(id)sender;
-(IBAction)onDelete:(id)sender;
-(IBAction)onUpDown:(id)sender;
-(void)setIsUp:(BOOL)isUp;
@end
